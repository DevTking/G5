package g5.org.g5.messages;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class AuthStatusRxMessage extends TransmitterMessage {
    int opcode = 0x5;
    public int authenticated;
    public int bonded;

    public AuthStatusRxMessage(byte[] packet) {
        if (packet.length >= 3) {
            if (packet[0] == opcode) {
                data = ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN);

                authenticated = data.get(1);
                bonded = data.get(2);

            }
        }
    }
}
