package SecureChat.network;

import SecureChat.Person;
import SecureChat.crypto.CryptoUtil;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static SecureChat.crypto.CryptoUtil.stringToBa;

public class Receiver {

    private static Thread receiveThread;
    private ServerSocket server;
    private ObservableList<String> messages;
    private Person person;
    private CryptoUtil cryptoUtil;

    public Receiver(Person person) {
        this.person = person;
        cryptoUtil = new CryptoUtil(person.getKey());

        receiveThread = new Thread(() -> {
            boolean running = true;
            while (running) {
                try {
                    this.server = new ServerSocket(person.getPort());
                    String data;
                    Socket client = this.server.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

                    while ((data = getString(in)) != null) {
                        processMessage(data);
                    }
                } catch (Exception e) {
                    running = false;
                    System.out.println(e.getMessage());
                }
            }
        });
        receiveThread.start();
    }

    /*
    Splits incoming message into nonce and cyphertext
    gets invalid size (23) nonce if nonce ends with 0, causing error at decryption
     */
    private void processMessage(String data) {
        data = data.substring(1);
        data = data.substring(0, data.length() - 1);
        int dividerIndex = data.indexOf("000");
        String nonce = data.substring(0, dividerIndex);
        String cypherText = data.substring(dividerIndex + 3, data.length());
        String message = cryptoUtil.decrypt(stringToBa(cypherText), stringToBa(nonce));
        Platform.runLater(() -> messages.add(person + ": " + message));     // runlater method is used to satisfy JavaFX thread
    }

    public void setMessageList(ObservableList<String> messages) {
        this.messages = messages;
    }

    public static void stop() {
        receiveThread.interrupt();
    }

    private String getString(BufferedReader in) throws IOException {
        char[] chars = new char[256];
        in.read(chars);
        String text = new String(chars).trim();
        return text;
    }
}
