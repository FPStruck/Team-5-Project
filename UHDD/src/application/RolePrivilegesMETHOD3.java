package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class RolePrivilegesMETHOD3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        String documentsFolder = System.getProperty("user.home") + "/Documents/";
        String usersFilePath = documentsFolder + "users.txt";
        String dataFilePath = documentsFolder + "data.txt";
        
        boolean continueLoop = true;
        
        while (continueLoop) {
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            
            String line;
            boolean found = false;
            boolean isAdmin = false;
            int lineCount = 0;
            
            try {
                Path usersPath = Paths.get(usersFilePath);
                
                // Create users file if it doesn't exist
                if (!Files.exists(usersPath)) {
                    Files.createFile(usersPath);
                }
                
                FileReader fileReader = new FileReader(usersFilePath);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                
                // Check if the user exists in file
                while ((line = bufferedReader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String fileUsername = parts[0];
                    String filePassword = parts[1];
                    String role = parts[2];
                    
                    if (fileUsername.equals(username) && filePassword.equals(password)) {
                        found = true;
                        isAdmin = role.equals("admin");
                        break;
                    }
                    
                    lineCount++;
                }
                
                bufferedReader.close();
                
                if (found) {
                    Path dataPath = Paths.get(dataFilePath);
                    
                    // Create data file if it doesn't exist
                    if (!Files.exists(dataPath)) {
                        Files.createFile(dataPath);
                    }
                    
                    fileReader = new FileReader(dataFilePath);
                    bufferedReader = new BufferedReader(fileReader);
                    
                    if (isAdmin) {
                        // Display entire file
                        while ((line = bufferedReader.readLine()) != null) {
                            System.out.println(line);
                        }
                    } else {
                        // Display first 3 lines of file
                        while ((line = bufferedReader.readLine()) != null && lineCount < 3) {
                            System.out.println(line);
                            lineCount++;
                        }
                    }
                    
                    bufferedReader.close();
                } else {
                    System.out.println("User not found.");
                    System.out.print("Enter role (admin/visitor): ");
                    String role = scanner.nextLine();
                    
                    FileWriter fileWriter = new FileWriter(usersFilePath, true);
                    fileWriter.write(username + "," + password + "," + role + "\n");
                    fileWriter.close();
                    
                    System.out.println("User created successfully.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            System.out.print("Continue (y/n)? ");
            String input = scanner.nextLine();
            
            continueLoop = input.equalsIgnoreCase("y");
        }
        
        scanner.close();
    }
}







