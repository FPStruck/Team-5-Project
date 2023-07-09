package application;

import java.util.Arrays;

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
    private static String clientSecret = "3rM8Q~t3Qz3MEYZJZdIsfYXetGfTqEIy3aiLCaav"; // this is the value not the ID
    private static String tenantId = "d57abc1a-51fe-44f9-919f-e8f214330064";
    private static String scope = "https://graph.microsoft.com/.default"; // this is for any tenant 
    
    public static void main(String[] args){
    	final ClientSecretCredential clientSecretCredential = new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .clientSecret(clientSecret) //required for web apps, do not set for native apps
                .tenantId(tenantId)
                .build();

            final TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(Arrays.asList(scope), clientSecretCredential);

            GraphServiceClient graphClient = GraphServiceClient.builder()
                            .authenticationProvider(tokenCredentialAuthProvider)
                            .buildClient();

            User user = new User();
            user.accountEnabled = true;
            user.displayName = "John Doe";
            user.mailNickname = "JD";
            user.userPrincipalName = "JohnDoe@uhdbitc309.onmicrosoft.com"; // this needs to be @uhdbitc309.onmicrosoft.com
            user.passwordPolicies = "DisablePasswordExpiration"; // optional
            PasswordProfile passwordProfile = new PasswordProfile();
            passwordProfile.forceChangePasswordNextSignIn = true; // false if the user does not need to change password
            passwordProfile.password = "xWwvJ]6NMw+bWH-d";
            user.passwordProfile = passwordProfile;
            
            graphClient.users()
                .buildRequest()
                .post(user);
	}
}