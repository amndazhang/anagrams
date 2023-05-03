import java.util.ArrayList;

public class Manager {
    private ArrayList<ServerThread> threads;

    public Manager() {
        threads = new ArrayList<ServerThread>();
    }

    public void addThread(ServerThread thread) {
        threads.add(thread);
    }

    public void broadcast(String message) {
        for (ServerThread thread : threads) {
            thread.send(message);
        }
    }

    public void broadcastObject(Object o) {
         for (ServerThread thread : threads) {
            thread.sendObj(o);
        }
    }
}
