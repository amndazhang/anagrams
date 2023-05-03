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
    private MyHashMap<Word, DLList<Word>> gameVersions;

    public ServerThread(Socket clientSocket, int number, Manager manager, int selected) {
        this.clientSocket = clientSocket;
        this.number = number;
        this.manager = manager;
        this.selected = selected;

        gameVersions = new MyHashMap<>();
        DLList<Word> vals = new DLList<>();
        vals.add(new Word("stings"));
        vals.add(new Word("signs"));
        vals.add(new Word("grin"));
        gameVersions.put(new Word("string"), vals);

        System.out.println("gameVeersions.get(0): in ctr" + gameVersions.toString());
        // gameVersions.put("string",
        // new{"strings","stings","string","girts","gists","grins","grist","grits","instr","rings","signs","sings","snits","sting","stirs","tings","gins","girt","gist","gits","grin","grit","inst","nits","rigs","ring","sign","sing","sins","sirs","sits","stir","ting","tins","trig","gin","git","its","nit","rig","sin","sir","sis","sit","tin"});
    }

    // 321,272,406

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

                outObj.reset();
                System.out.println("gameVeersions.get(0): " + gameVersions.get(0));
                outObj.writeObject(gameVersions.get(gameVersions.keySet().toDLList().get(0)));
                while (true) {
                    Object o = inObj.readObject();
                    if (o instanceof String) {
                        String s = (String)o;
                        System.out.println(s);
                    }
                    // Object o = inObj.readObject();
                    /*
                     * if (o instanceof Square[][]) {
                     * 
                     * grid = (Square[][])o;
                     * manager.broadcastObject(grid);
                     * }
                     */
                    // if (o instanceof String) {
                    // String s = (String)o;
                    // if (number == selected && s.contains(word)) {
                    // manager.broadcastObject(s.substring(0, s.indexOf(":")) + " wins!");
                    // clientSocket.close();

                    // }
                    // else {
                    // manager.broadcastObject("_" + name + ": " + o);
                    // }
                    // }

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