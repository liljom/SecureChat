package SecureChat.network;

import SecureChat.Person;
import SecureChat.crypto.CryptoUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import static SecureChat.crypto.CryptoUtil.baToString;

public class Sender {
    private Socket socket;
    private CryptoUtil cryptoUtil;

    public Sender(Person person) {
        cryptoUtil = new CryptoUtil(person.getKey());
        try {
            this.socket = new Socket(person.getIP(), person.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message) {
        try {
            PrintWriter sender = new PrintWriter(this.socket.getOutputStream(), false);
            String cypherText = cryptoUtil.encrypt(message);

            String toSend = createText(baToString(cryptoUtil.getNonce()), cypherText);

            sender.println(toSend);
            sender.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String createText(String nonce, String cypherText) {
        String toSend = null;
        try {
            toSend = new String(new byte[128], "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        toSend += "X";
        toSend += nonce;
        toSend += "000";
        toSend += cypherText;
        toSend += "X";
        return toSend;
    }
}
