package g5.org.g5.messages;

import g5.org.g5.messages.CRC;
import g5.org.g5.messages.TransmitterMessage;

import java.nio.ByteBuffer;


public class TransmitterTimeTxMessage extends TransmitterMessage {
    byte opcode = 0x24;
    byte[] crc = CRC.calculate(opcode);

    public TransmitterTimeTxMessage() {
        data = ByteBuffer.allocate(3);
        data.put(opcode);
        data.put(crc);
        byteSequence = data.array();
    }
}
