package g5.org.g5.messages;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class KeepAliveTxMessage extends TransmitterMessage {
    public static final int opcode = 0x6;
    private int time;


    public KeepAliveTxMessage(int time) {
        this.time = time;

        data = ByteBuffer.allocate(2);
        data.put(new byte[]{(byte) opcode, (byte) this.time});
        byteSequence = data.order(ByteOrder.LITTLE_ENDIAN).array();
    }
}