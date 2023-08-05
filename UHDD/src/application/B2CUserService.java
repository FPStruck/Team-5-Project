package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.PasswordProfile;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;

import org.springframework.stereotype.Service;

@Service
public class B2CUserService {
	// the following details is from the app registration page from Azure AD B2C
    private static String clientId = "93622b73-f524-4e0a-abd5-c484ed4cf9bf";
    private static String clientSecret = "Z1a8Q~LuPsWAkbs0r1L-ajhC-h_2zEdewDyDJblF"; // this is the value not the ID
    private static String tenantId = "d57abc1a-51fe-44f9-919f-e8f214330064";
    private static String scope = "https://graph.microsoft.com/.default"; // this is for any tenant 
    List<String> emails = new ArrayList();
    
    public void createUser(String username, String password, String email) {
        final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
            .clientId(clientId)
            .clientSecret(clientSecret) //required for web apps, do not set for native apps
            .tenantId(tenantId)
            .build();

        final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(Arrays.asList(scope), clientSecretCredential);
        
        // from https://learn.microsoft.com/en-us/graph/api/user-post-users?view=graph-rest-1.0&tabs=java
        GraphServiceClient graphClient = GraphServiceClient.builder()
                        .authenticationProvider(tokenCredentialAuthProvider)
                        .buildClient();

        User user = new User();
        user.accountEnabled = true;
        user.displayName = username;
        user.mailNickname = username;
        user.userPrincipalName = username + "@uhdbitc309.onmicrosoft.com"; // abc is the AD B2C name
        System.out.println(user.userPrincipalName);
        user.passwordPolicies = "DisablePasswordExpiration"; // optional
        PasswordProfile passwordProfile = new PasswordProfile();
        passwordProfile.forceChangePasswordNextSignIn = false; // false if the user does not need to change password
        passwordProfile.password = password; // A password that's at least 8 to 64 characters. It requires 3 out of 4 of lowercase, uppercase, numbers, or symbols.
        user.passwordProfile = passwordProfile;
        
        emails.add(email);
        System.out.println("Emails: " + emails);
        user.otherMails = emails;
        
        graphClient.users()
            .buildRequest()
            .post(user);
    }
    
}