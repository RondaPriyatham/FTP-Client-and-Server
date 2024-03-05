import java.io.*;
import java.net.*;

public class Client {
    public Socket client = null;
    public DataInputStream dis = null;
    public DataOutputStream dos = null;
    public FileInputStream fis = null;
    public FileOutputStream fos = null;
    public BufferedReader br = null;
    public String inputFromUser = "";

    public static void main(String[] args) {
	// This takes the port number from args in command line
	int port=Integer.parseInt(args[0]);
        Client c = new Client();
        c.doConnections(port);
    }

    public void doConnections(int port) {
        try {
            InputStreamReader isr = new InputStreamReader(System.in);
            br = new BufferedReader(isr);
            // Assigns the port number read from the arguments
	    // Enter 8888 as port number as its the port server runs on
            client = new Socket("127.0.0.1", port);
            System.out.println("Connection established with the server");
            // Setting up input and output streams
            dis = new DataInputStream(client.getInputStream());
            dos = new DataOutputStream(client.getOutputStream());

        } catch (Exception e) {
            System.out.println("Unable to Connect to Server");
        }

        while (true) {
            try {
                // Prompt the user for a choice
                System.out.println("Please Make a Choice : \n1. Upload file \n2. Get file \nYour Choice: ");
                inputFromUser = br.readLine();
                int i = Integer.parseInt(inputFromUser);
                switch (i) {
                    case 1:
                        sendFile();
                        break;
                    case 2:
                        receiveFile();
                        break;
                    default:
                        System.out.println("Invalid Option !");
                }
            } catch (Exception e) {
                System.out.println("Some Error Occurred!");
            }
        }
    }

    public void sendFile() {
        try {
            String filename = "";
            File file;
            byte[] buffer;
            System.out.println("Enter the filename: ");
            filename = br.readLine();
            file = new File(filename);
            if (file.isFile()) {
                // Open input stream to read the file
                fis = new FileInputStream(file);

                // This sends a command to the server indicating file upload
                dos.writeUTF("FILE_SEND_FROM_CLIENT");
                // Send the filename to the server
                dos.writeUTF(filename);
                
                // Send the size of the file to the server
                long fileSize = file.length();
                dos.writeLong(fileSize);

                // Initialize buffer for reading file in chunks
                buffer = new byte[4 * 1024];
                int bytesRead;

                // Read file in chunks and send to the server
                while ((bytesRead = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                    dos.flush();
                }

                // Close input stream after file is sent
                fis.close();
                System.out.println("File Send Successful!");
            } else {
                System.out.println("File Not Found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receiveFile() {
        try {
            String filename = "";
            System.out.println("Enter the filename: ");
            filename = br.readLine();
           
            // This sends a command to the server indicating file download
            dos.writeUTF("DOWNLOAD_FILE");
            // Send the filename to the server
            dos.writeUTF(filename);

            // Receive the size of the file from the server
            long fileSize = dis.readLong();
            if (fileSize == 0) {
                System.out.println("No Such File");
            } else {
                // Open output stream to write the received file
                fos = new FileOutputStream(filename);

                // Initialized buffer for reading file in chunks
                byte[] buffer = new byte[4 * 1024];
                int bytesRead;

                // Read file in chunks and write to the local file
                while (fileSize > 0 && (bytesRead = dis.read(buffer, 0, buffer.length)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                    fileSize -= bytesRead;
                }

                // Close output stream after file is received
                fos.close();
                System.out.println("File Received Successfully!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
