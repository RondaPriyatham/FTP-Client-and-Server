import java.io.*;
import java.net.*;

class Server {
    ServerSocket server = null;
    Socket client = null;

    public static void main(String[] arg) {
        Server s = new Server();
        s.doConnections();
    }

    public void doConnections() {

        try {
            // Create a ServerSocket listening on port 8888
            server = new ServerSocket(8888);
            System.out.println("Listening on the port 8888...");
            while (true) {
                // Accept client connections on port 8888
                client = server.accept();
                System.out.println("Connected with a client");
                // Start a new thread for each client, this allows multiple clients listening to the same server
                ClientThread ct = new ClientThread(client);
                ct.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ClientThread extends Thread {
    public Socket client = null;
    public DataInputStream dis = null;
    public DataOutputStream dos = null;
    public FileInputStream fis = null;
    public FileOutputStream fos = null;
    public BufferedReader br = null;
    public String inputFromUser = "";
    public File file = null;

    public ClientThread(Socket c) {
        try {
            client = c;
            // Setting up input and output streams for the client
            dis = new DataInputStream(c.getInputStream());
            dos = new DataOutputStream(c.getOutputStream());

        } catch (Exception e) {

        }
    }

    public void run() {
        while (true) {
            try {
                // Read the command sent by the client
                String input = dis.readUTF();
                String filename = "";

                if (input.equals("FILE_SEND_FROM_CLIENT")) {
                    // If client wants to send a file to the server
                    filename = dis.readUTF();
		    // This line can read the file name if a file path is received as a stream
		    int ind = filename.lastIndexOf('\\');
	 	    String name = "";
	   	    if(ind > 0){
			name = filename.substring(ind + 1);
		    } else{
			name=filename;
		    }
		    // Initialize file size and create the file to write
                    long fileSize = dis.readLong();
                    fos = new FileOutputStream("new"+name);

                    byte[] buffer = new byte[4 * 1024]; // Buffer to store chunks of the file
                    int bytesRead;

                    // Read file in chunks and write to the local file
                    while (fileSize > 0 && (bytesRead = dis.read(buffer, 0, buffer.length)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                        fileSize -= bytesRead;
                    }

                    fos.close();
                    System.out.println("File Received from Client: " + filename);
                } else if (input.equals("DOWNLOAD_FILE")) {
                    // If client wants to download a file from the server
                    filename = dis.readUTF();
                    file = new File(filename);

                    if (file.isFile()) {
                        fis = new FileInputStream(file);

                        // Send the size of the file to the client
                        dos.writeLong(file.length());

                        byte[] buffer = new byte[4 * 1024]; // Buffer to store chunks of the file
                        int bytesRead;

                        // Read file in chunks and send to the client
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            dos.write(buffer, 0, bytesRead);
                            dos.flush();
                        }
			// Close the input stream after the file transactions are over
                        fis.close();

                        System.out.println("File Sent to Client: " + filename);
                    } else {
                        // Send 0 to indicate that the file is not found
                        dos.writeLong(0);
                    }
                } else {
                    System.out.println("Error at Server");
                }
            } catch (Exception e) {

            }
        }
    }
}
