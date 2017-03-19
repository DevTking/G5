package g5.org.g5.messages;

import java.nio.ByteBuffer;

public class DisconnectTxMessage extends TransmitterMessage {
    byte opcode = 0x09;
     public DisconnectTxMessage() {
        data = ByteBuffer.allocate(1);
        data.put(opcode);

        byteSequence = data.array();

    }
}

