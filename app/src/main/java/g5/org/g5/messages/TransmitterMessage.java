package g5.org.g5.messages;

import java.nio.ByteBuffer;

public abstract class TransmitterMessage {
    public byte[] byteSequence = null;
    public ByteBuffer data = null;

    public void setData() {
        byte[] newData;

    }

    static int getUnsignedShort(ByteBuffer data) {
        return ((data.get() & 0xff) + ((data.get() & 0xff) << 8));
    }

    static int getUnsignedByte(ByteBuffer data) {
        return ((data.get() & 0xff));
    }

}
