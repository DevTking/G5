package g5.org.g5.messages;


public enum TransmitterStatus {
    UNKNOWN, BRICKED, LOW, OK;

    public static TransmitterStatus getBatteryLevel(int b) {
        if (b > 0x81) {
            return BRICKED;
        }
        else {
            if (b == 0x81) {
                return LOW;
            }
            else if (b == 0x00) {
                return OK;
            }
            else {
                return UNKNOWN;
            }
        }
    }
}
