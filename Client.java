import java.net.*;
import java.io.*;
import javax.swing.JFrame;

public class Client {
	public static void main(String[] args) throws IOException {

		JFrame frame = new JFrame("Server");

		ClientScreen sc = new ClientScreen();
		frame.add(sc);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		sc.poll();

	}
}