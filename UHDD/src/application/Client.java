package application;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 12345); // Server IP address and port

            // Create input and output streams for data transfer
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

            // Send data to the server
            String dataToServer = "Hello Server";
            writer.write(dataToServer);
            writer.newLine();
            writer.flush();
            System.out.println("Data sent to server.");

            // Receive response from the server
            String responseFromServer = "hi";
            System.out.println("Response received from server: " + responseFromServer);

            // Close the connection
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}










