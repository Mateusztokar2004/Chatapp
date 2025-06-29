package kolos2022;

public class Main {
    public static void main(String[] args) {
        WordBag bag = new WordBag();
        bag.populate();                         // <-- BRAKOWAŁO
        try {
            Server srv = new Server(5010, bag);
            srv.startSending();                 // timer co 5 s
            srv.start();                        // wątek accept()
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}