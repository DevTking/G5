package g5.org.g5.messages;

import java.nio.ByteBuffer;

public class SensorTxMessage extends TransmitterMessage {
    byte opcode = 0x2e;
    byte[] crc = CRC.calculate(opcode);

    public SensorTxMessage() {
        data = ByteBuffer.allocate(3);
        data.put(opcode);
        data.put(crc);
        byteSequence = data.array();
    }
}
