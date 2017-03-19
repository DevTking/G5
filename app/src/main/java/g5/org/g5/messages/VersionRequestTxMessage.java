package g5.org.g5.messages;

import java.nio.ByteBuffer;


public class VersionRequestTxMessage extends TransmitterMessage {

    byte opcode = 0x4A;
    private byte[] crc = CRC.calculate(opcode);

    public VersionRequestTxMessage() {
        data = ByteBuffer.allocate(3);
        data.put(opcode);
        data.put(crc);
        byteSequence = data.array();
    }
}

