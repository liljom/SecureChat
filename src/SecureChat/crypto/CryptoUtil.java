package SecureChat.crypto;

import org.abstractj.kalium.NaCl;
import org.abstractj.kalium.crypto.SecretBox;

import java.io.UnsupportedEncodingException;

public class CryptoUtil {

    private SecretBox sb;
    private NaCl.Sodium sodium;
    private byte[] nonce = new byte[NaCl.Sodium.CRYPTO_SECRETBOX_XSALSA20POLY1305_NONCEBYTES];

    public CryptoUtil(byte[] key) {
        sodium = NaCl.sodium();
        sb = new SecretBox(key);
    }

    private byte[] generateNonce() {
        sodium.randombytes(nonce, nonce.length);
        return nonce;
    }

    public byte[] getNonce() {
        return nonce;
    }

    public String encrypt(String message) {
        byte[] cypherText = sb.encrypt(generateNonce(), stringToBa(message));
        return baToString(cypherText);
    }

    public String decrypt(byte[] cypherText, byte[] nonce) {
        byte[] msg = sb.decrypt(nonce, cypherText);
        return baToString(msg);
    }

    public static String baToString(byte[] bytes) {
        String s = null;
        try {
            s = new String(bytes, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static byte[] stringToBa(String s) {
        byte[] bytes = null;
        try {
            bytes = s.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
