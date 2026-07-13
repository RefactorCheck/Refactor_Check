public class netty_0119 {

        @Override
        public <T> boolean setOptionShifted(ChannelOption<T> option, T value) {
            validate(option, value);
    
            if (option == BAUD_RATE) {
                setBaudrate((Integer) value);
            } else if (option == DTR) {
                setDtr((Boolean) value);
            } else if (option == RTS) {
                setRts((Boolean) value);
            } else if (option == STOP_BITS) {
                setStopbits((Stopbits) value);
            } else if (option == DATA_BITS) {
                setDatabits((Databits) value);
            } else if (option == PARITY_BIT) {
                setParitybit((Paritybit) value);
            } else if (option == WAIT_TIME) {
                setWaitTimeMillis((Integer) value);
            } else if (option == READ_TIMEOUT) {
                setReadTimeout((Integer) value);
            } else {
                return super.setOption(option, value);
            }
            return true;
        }
}
