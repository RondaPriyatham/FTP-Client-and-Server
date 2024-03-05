# FTP-Client-and-Server
FTP Server and Client with file transfer functionalities using Java. Run Server.java and Client.java for testing, supporting both upload and download operations with easy-to-follow instructions.

The codes of the FTP server and client are placed in their specific folders. 
The files are sent in a series of chunks to transfer the larger files.
Testing assistance:
Run both Server.java and Client.java in separate terminals.
Start the server first and then connect the client with it.
While establishing the connection with the server, give the port number in args when running the client.java
-> java Client 8888
Once the connection is established you will be given two choices of operations.
Enter the numerical of the operation you want followed by the file name or path that you want to transfer.
To test both functionalities, the demo files have been placed in their designated folders.

1. To test the Upload file, type the file name as <uploadTestFile.pptx> or specify the path to that file.
   Once the operation is finished, you can find the file in the Server's directory with the "new" prefix added to the file.
2. To test the get functionality type the file name as <downloadTestFile.pptx> for testing purposes or else you can place any file in the same directory of the Server class and retrieve the file.
   Once the operation is finished, you can find the file in the Client's directory.

(you can further test the codes by placing them in the directory of your choice and with filepaths of your test cases.)
Note: ClientThread class is added to the Server code to allow multiple client connections to the server. You can find the ClientThread.class file in the FTP Server folder.
