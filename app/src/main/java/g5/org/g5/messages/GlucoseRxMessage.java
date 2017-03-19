package g5.org.g5.messages;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GlucoseRxMessage extends TransmitterMessage {

    public static final byte opcode = 0x31;
    public TransmitterStatus status;
    public int status_raw;
    public int timestamp;
    public int unfiltered;
    public int filtered;
    public int sequence; // : UInt32
    public boolean glucoseIsDisplayOnly; // : Bool
    public int glucose; // : UInt16
    public int state; //: UInt8
    public int trend; // : Int8


    public GlucoseRxMessage(byte[] packet) {
          if (packet.length >= 14) {
            // TODO check CRC??
            if (packet[0] == opcode) {
                data = ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN);

                status_raw = data.get(1);
                status = TransmitterStatus.getBatteryLevel(data.get(1));
                sequence = data.getInt(2);
                timestamp = data.getInt(6);


                int glucoseBytes = data.getShort(10); // check signed vs unsigned!!
                glucoseIsDisplayOnly = (glucoseBytes & 0xf000) > 0;
                glucose = glucoseBytes & 0xfff;

                state = data.get(12);
                trend = data.get(13);
                if (glucose > 13) {
                    unfiltered = glucose * 1000;
                    filtered = glucose * 1000;
                } else {
                    filtered = glucose;
                    unfiltered = glucose;
                }


            }
        } else {
        }
    }
}
