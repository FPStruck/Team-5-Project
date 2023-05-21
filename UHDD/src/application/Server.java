package application;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(12345); // Server port
            System.out.println("Server started. Waiting for a client...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            // Create input and output streams for data transfer
            InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));

            // Read data from the client
            String dataFromClient = reader.readLine();
            System.out.println("Data received from client: " + dataFromClient);

            // Process the data (e.g., perform some operations)

            // Send a response back to the client
            String responseToClient = "Hello Client";
            writer.write(responseToClient);
            writer.newLine();
            writer.flush();
            System.out.println("Response sent to client.");

            // Close the connection
            clientSocket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}






