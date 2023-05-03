import java.net.*;
import java.io.*;

public class Server {
	public static void main(String[] args) throws IOException {
		int portNumber = 1024;
		ServerSocket serverSocket = new ServerSocket(portNumber);
		int count = 1;
		 Manager manager = new Manager();
		 int selected = 1;
		//This loop will run and wait for one connection at a time.
		while(true){
			System.out.println("Waiting for a connection");

			//Wait for a connection.
			Socket clientSocket = serverSocket.accept();

			//Once a connection is made, run the socket in a ServerThread.
			 ServerThread serverThread = new ServerThread(clientSocket, count, manager, selected);
			Thread thread = new Thread(serverThread);
			manager.addThread(serverThread);
			count++;
			thread.start();
		}
	}
}
