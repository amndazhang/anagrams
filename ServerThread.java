import java.net.*;
import java.io.*;

public class ServerThread implements Runnable {
    private Socket clientSocket;
    private int number;
    private Manager manager;
    private ObjectOutputStream outObj;
    private DLList<String> players = new DLList<String>();
    private DLList<String> scores = new DLList<String>();
    private int selected;

    private PrintWriter out;
    private DLList<String> words = new DLList<>();

    public ServerThread(Socket clientSocket, int number, Manager manager, int selected) {
        this.clientSocket = clientSocket;
        this.number = number;
        this.manager = manager;
        this.selected = selected;

        words.add("tsignr");
        words.add("lteicn");
        words.add("armsfe");
        words.add("spneal");
        words.add("tderah");
        words.add("ecstok");
    }

    public void run() {

        try {
            outObj = new ObjectOutputStream(clientSocket.getOutputStream());

            ObjectInputStream inObj = new ObjectInputStream(clientSocket.getInputStream());

            try {

                outObj.reset();

                int n = (int) (Math.random() * words.size());
                manager.broadcastObject("word " + words.get(n));
                words.remove(n);

                while (true) {
                    Object o = inObj.readObject();
                    if (o instanceof String) {
                        String str = (String) o;

                        if (str.contains(" ")) {
                            // endgame scores
                            scores.add(str);

                            manager.broadcastObject(scores.toString());
                        } else {
                            // names
                            players.add(str);
                        }
                    } else if (o instanceof Boolean) {
                        // when game restart and need new word
                        n = (int) (Math.random() * words.size());
                        manager.broadcastObject("word " + words.get(n));
                        words.remove(n);
                    }
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
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
