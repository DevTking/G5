package g5.org.g5.messages;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.UUID;


public class AuthRequestTxMessage extends TransmitterMessage {
    byte opcode = 0x1;
    public byte[] singleUseToken;
    byte endByte = 0x2;

    public static byte[] getRandomKey() {
        byte[] keybytes = new byte[16];
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(keybytes);
        return keybytes;
    }

    public AuthRequestTxMessage() {
        int token_size = 8;
        byte[] uuidBytes = getRandomKey();
        UUID uuid = UUID.nameUUIDFromBytes(uuidBytes);

        try {
            uuidBytes = uuid.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ByteBuffer bb = ByteBuffer.allocate(token_size);
        bb.put(uuidBytes, 0, token_size);
        singleUseToken = bb.array();

        // Create the byteSequence.
        data = ByteBuffer.allocate(token_size+2);
        data.put(opcode);
        data.put(singleUseToken);
        data.put(endByte);

        byteSequence = data.array();
    }
}

