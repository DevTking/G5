package g5.org.g5.messages;

import java.nio.ByteBuffer;


public class BatteryInfoTxMessage extends TransmitterMessage {

     byte opcode = 0x22;
    private byte[] crc = CRC.calculate(opcode);

    public BatteryInfoTxMessage() {
        data = ByteBuffer.allocate(3);
        data.put(opcode);
        data.put(crc);
        byteSequence = data.array();
//        UserError.Log.e(TAG, "BatteryInfoTx dbg: " + JoH.bytesToHex(byteSequence));
    }
}

