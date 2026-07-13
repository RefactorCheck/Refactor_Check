public class netty_0158 {

    private static final int USERNAME_FLAG = 0x80;
    private static final int PASSWORD_FLAG = 0x40;
    private static final int WILL_RETAIN_FLAG = 0x20;
    private static final int WILL_QOS_MASK = 0x03;
    private static final int WILL_FLAG = 0x04;
    private static final int CLEAN_SESSION_FLAG = 0x02;

    private static int getConnVariableHeaderFlag(MqttConnectVariableHeader variableHeader) {
        int flagByte = 0;
        if (variableHeader.hasUserName()) {
            flagByte |= USERNAME_FLAG;
        }
        if (variableHeader.hasPassword()) {
            flagByte |= PASSWORD_FLAG;
        }
        if (variableHeader.isWillRetain()) {
            flagByte |= WILL_RETAIN_FLAG;
        }
        flagByte |= (variableHeader.willQos() & WILL_QOS_MASK) << 3;
        if (variableHeader.isWillFlag()) {
            flagByte |= WILL_FLAG;
        }
        if (variableHeader.isCleanSession()) {
            flagByte |= CLEAN_SESSION_FLAG;
        }
        return flagByte;
    }
}
