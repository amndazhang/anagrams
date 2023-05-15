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
    private DLList<String> players = new DLList<String>();
    private DLList<String> scores = new DLList<String>();
    private int selected;

    private PrintWriter out;
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

            // outObj.writeObject("Connection Successful!");

            ObjectInputStream inObj = new ObjectInputStream(clientSocket.getInputStream());

            try {
                String name = (String) inObj.readObject();
                players.add(name);
                

                outObj.reset();
                
                System.out.println("word "+words[0]);
                manager.broadcastObject("word "+words[0]);

                while (true) {
                    Object o = inObj.readObject();
                    if (o instanceof String) {
                        String str = (String) o;

                        scores.add(str);
                        System.out.println("scores: " + scores.toString());
                        System.out.println("players: " + players.toString());
                        if(scores.size() == players.size()){
                            manager.broadcastObject(scores.toString());
                        }
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
