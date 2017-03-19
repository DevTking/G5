package g5.org.g5.messages;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;


public class BatteryInfoRxMessage extends TransmitterMessage {

    public static final byte opcode = 0x23;

    public int status;
    public int voltagea;
    public int voltageb;
    public int resist;
    public int runtime;
    public int temperature;

    public BatteryInfoRxMessage(byte[] packet) {
        if (packet.length >= 12) {
            data = ByteBuffer.wrap(packet).order(ByteOrder.LITTLE_ENDIAN);
            if (data.get() == opcode) {
                status = data.get();
                voltagea = getUnsignedShort(data);
                voltageb = getUnsignedShort(data);
                resist = getUnsignedShort(data);
                runtime = getUnsignedByte(data);
                temperature = data.get(); // not sure if signed or not, but <0c or >127C seems unlikely!
            } else {
//                UserError.Log.wtf(TAG, "Invalid opcode for BatteryInfoRxMessage");
            }
        }
    }

    public String toString() {
        return String.format(Locale.US, "Status: %s / VoltageA: %d / VoltageB: %d / Resistance: %d / Run Time: %d / Temperature: %d",
                TransmitterStatus.getBatteryLevel(status).toString(), voltagea, voltageb, resist, runtime, temperature);
    }

}