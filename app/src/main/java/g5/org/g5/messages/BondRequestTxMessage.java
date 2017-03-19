package g5.org.g5.messages;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class BondRequestTxMessage extends TransmitterMessage {
    byte opcode = 0x7;


    public BondRequestTxMessage() {
        data = ByteBuffer.allocate(1);
        data.put(opcode);
        byteSequence = data.array();
 //       UserError.Log.d(TAG,"New BONDRequestTxMessage: "+ JoH.bytesToHex(byteSequence));
    }
}

