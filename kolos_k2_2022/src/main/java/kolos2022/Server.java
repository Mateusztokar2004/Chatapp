package kolos2022;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server extends Thread {

    private final ServerSocket serverSocket;
    private final List<ClientThread> clients = new ArrayList<>();
    private final WordBag wordBag;

    public Server(int port, WordBag bag) throws IOException {
        this.wordBag = bag;
        this.serverSocket = new ServerSocket(port);
        System.out.println("Serwer słów nasłuchuje na porcie " + port);
    }

    /* cykliczne wysyłanie */
    public void startSending() {
        Timer t = new Timer(true);
        t.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() { broadcast(wordBag.get()); }
        }, 0, 5_000);
    }

    @Override public void run() {
        while (true) {
            try {
                Socket s = serverSocket.accept();
                ClientThread ct = new ClientThread(s, this);
                clients.add(ct);
                ct.start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    void removeClient(ClientThread c) { clients.remove(c); }
    void broadcast(String msg)        { clients.forEach(cl -> cl.send(msg)); }
}