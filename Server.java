import java.net.*;
import java.io.*;

public class Server {
	public static void main(String[] args) throws IOException {
		int portNumber = 1024;
		ServerSocket serverSocket = new ServerSocket(portNumber);
		int count = 1;
		Manager manager = new Manager();
		int selected = 1;
		while (true) {
			Socket clientSocket = serverSocket.accept();
			ServerThread serverThread = new ServerThread(clientSocket, count, manager, selected);
			Thread thread = new Thread(serverThread);
			manager.addThread(serverThread);
			count++;
			thread.start();
		}
	}
}
