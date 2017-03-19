package g5.org.g5.messages;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class TransmitterTimeRxMessage extends TransmitterMessage {
    byte opcode = 0x25;
    public TransmitterStatus status;
    public int currentTime;
    public int sessionStartTime;

    public TransmitterTimeRxMessage(byte[] packet) {
        if (packet.length >= 10) {
            if (packet[0] == opcode) {
                data = ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN);

                status = TransmitterStatus.getBatteryLevel(data.get(1));
                currentTime = data.getInt(2);
                sessionStartTime = data.getInt(6);
            }
        }
    }
}
