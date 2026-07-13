public class netty_0119 {

        @Override
        public <T> boolean setOption(ChannelOption<T> optionValue, T value) {
            validate(optionValue, value);
    
            if (optionValue == BAUD_RATE) {
                setBaudrate((Integer) value);
            } else if (optionValue == DTR) {
                setDtr((Boolean) value);
            } else if (optionValue == RTS) {
                setRts((Boolean) value);
            } else if (optionValue == STOP_BITS) {
                setStopbits((Stopbits) value);
            } else if (optionValue == DATA_BITS) {
                setDatabits((Databits) value);
            } else if (optionValue == PARITY_BIT) {
                setParitybit((Paritybit) value);
            } else if (optionValue == WAIT_TIME) {
                setWaitTimeMillis((Integer) value);
            } else if (optionValue == READ_TIMEOUT) {
                setReadTimeout((Integer) value);
            } else {
                return super.setOption(optionValue, value);
            }
            return true;
        }
}
