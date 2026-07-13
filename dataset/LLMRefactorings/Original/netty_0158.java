public class netty_0158 {

        private static int getConnVariableHeaderFlag(MqttConnectVariableHeader variableHeader) {
            int flagByte = 0;
            if (variableHeader.hasUserName()) {
                flagByte |= 0x80;
            }
            if (variableHeader.hasPassword()) {
                flagByte |= 0x40;
            }
            if (variableHeader.isWillRetain()) {
                flagByte |= 0x20;
            }
            flagByte |= (variableHeader.willQos() & 0x03) << 3;
            if (variableHeader.isWillFlag()) {
                flagByte |= 0x04;
            }
            if (variableHeader.isCleanSession()) {
                flagByte |= 0x02;
            }
            return flagByte;
        }
}
