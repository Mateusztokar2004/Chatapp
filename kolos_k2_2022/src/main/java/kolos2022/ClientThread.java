package kolos2022;

import java.io.*;
import java.net.Socket;

class ClientThread extends Thread {

    private final Socket socket;
    private final Server server;
    private PrintWriter writer;

    ClientThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    @Override public void run() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(socket.getInputStream()))) {

            writer = new PrintWriter(socket.getOutputStream(), true);
            while (br.readLine() != null) { /* klient czyta, ale nas nie obchodzi */ }

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            server.removeClient(this);
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    void send(String msg) { if (writer != null) writer.println(msg); }
}