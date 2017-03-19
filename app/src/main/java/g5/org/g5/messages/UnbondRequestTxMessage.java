package g5.org.g5.messages;

import java.nio.ByteBuffer;

public class UnbondRequestTxMessage extends TransmitterMessage {
    byte opcode = 0x6;

    public UnbondRequestTxMessage() {
        data = ByteBuffer.allocate(1);
        data.put(opcode);

        byteSequence = data.array();
    }
}
