package g5.org.g5.messages;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SensorRxMessage extends TransmitterMessage {
    byte opcode = 0x2f;
    public TransmitterStatus status;
    public int timestamp;
    public int unfiltered;
    public int filtered;

    public SensorRxMessage(byte[] packet) {

        if (packet.length >= 14) {
            if (packet[0] == opcode) {
                data = ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN);

                status = TransmitterStatus.getBatteryLevel(data.get(1));
                timestamp = data.getInt(2);

                unfiltered = data.getInt(6);
                filtered = data.getInt(10);
            }
        }
    }
}
