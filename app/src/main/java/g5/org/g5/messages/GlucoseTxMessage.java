package g5.org.g5.messages;


import java.nio.ByteBuffer;

public class GlucoseTxMessage extends TransmitterMessage {

    byte opcode = 0x30;
    byte[] crc = CRC.calculate(opcode);

    public GlucoseTxMessage() {
        data = ByteBuffer.allocate(3);
        data.put(opcode);
        data.put(crc);
        byteSequence = data.array();
    }
}

