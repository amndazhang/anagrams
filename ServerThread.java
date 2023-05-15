import java.net.*;
import java.nio.channels.SelectableChannel;
import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.*;

public class ServerThread implements Runnable {
    private Socket clientSocket;
    private int number;
    private Manager manager;
    private ObjectOutputStream outObj;
    private int selected;

    private PrintWriter out;
    // private Square[][] grid;
    private String[] words = { "tsignr" };

    public ServerThread(Socket clientSocket, int number, Manager manager, int selected) {
        this.clientSocket = clientSocket;
        this.number = number;
        this.manager = manager;
        this.selected = selected;

    }

    public void run() {

        try {
            outObj = new ObjectOutputStream(clientSocket.getOutputStream());
            // out = new PrintWriter(clientSocket.getOutputStream(), true);
            // BufferedReader in = new BufferedReader(new
            // InputStreamReader(clientSocket.getInputStream()));

            outObj.writeObject("Connection Successful!");

            ObjectInputStream inObj = new ObjectInputStream(clientSocket.getInputStream());

            try {
                String name = (String) inObj.readObject();
                manager.broadcastObject("_" + name + " logged in");
                String word = "";
                // Sends a message
                System.out.println("hi");
                // word = words[(int)(Math.random() * words.length)];
                outObj.reset();
                // outObj.writeObject("drawing");
                // outObj.reset();
                // word = words[(int)(Math.random() * words.length)];
                // outObj.writeObject("word "+word);
                System.out.println("word "+words[0]);
                manager.broadcastObject("word "+words[0]);
                // manager.broadcastObject("_" + name + " is drawing");

                // grid = new Square[400][400];
                // outObj.reset();
                // outObj.writeObject(grid);
                while (true) {
                    Object o = inObj.readObject();
                    // if (o instanceof Square[][]) {

                    // grid = (Square[][])o;
                    // manager.broadcastObject(grid);
                    // }
                    // else
                    if (o instanceof String) {
                        // String s = (String) o;
                        // if (number == selected && s.contains(word)) {
                        //     manager.broadcastObject(s.substring(0, s.indexOf(":")) + " wins!");
                        //     clientSocket.close();

                        // } else {
                        //     manager.broadcastObject("_" + name + ": " + o);
                        // }
                    }

                    // try {
                    // } catch (Exception e) {

                    // }
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
            // Clears and close the output stream.
            // out.flush();
            // out.close();
            // System.out.println(Thread.currentThread().getName() + ": connection
            // closed.");
        } catch (IOException ex) {
            System.out.println("Error listening for a connection");
            System.out.println(ex.getMessage());
        }
    }

    public void send(String msg) {
        out.println(msg);
    }

    public void sendObj(Object o) {
        try {
            outObj.reset();
            outObj.writeObject(o);
        } catch (Exception e) {

        }
    }
}
