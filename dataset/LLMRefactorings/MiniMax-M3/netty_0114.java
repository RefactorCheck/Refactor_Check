public class netty_0114 {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getOption(ChannelOption<T> option) {
        T value;
        if ((value = getIntOption(option, SO_RCVBUF, getReceiveBufferSize())) != null) return value;
        if ((value = getIntOption(option, SO_SNDBUF, getSendBufferSize())) != null) return value;
        if ((value = getBooleanOption(option, TCP_NODELAY, isTcpNoDelay())) != null) return value;
        if ((value = getBooleanOption(option, SO_KEEPALIVE, isKeepAlive())) != null) return value;
        if ((value = getBooleanOption(option, SO_REUSEADDR, isReuseAddress())) != null) return value;
        if ((value = getIntOption(option, SO_LINGER, getSoLinger())) != null) return value;
        if ((value = getIntOption(option, IP_TOS, getTrafficClass())) != null) return value;
        if ((value = getBooleanOption(option, ALLOW_HALF_CLOSURE, isAllowHalfClosure())) != null) return value;
        if ((value = getIntOption(option, SO_SNDLOWAT, getSndLowAt())) != null) return value;
        if ((value = getBooleanOption(option, TCP_NOPUSH, isTcpNoPush())) != null) return value;
        if ((value = getBooleanOption(option, ChannelOption.TCP_FASTOPEN_CONNECT, isTcpFastOpenConnect())) != null) return value;
        return super.getOption(option);
    }

    @SuppressWarnings("unchecked")
    private <T> T getIntOption(ChannelOption<T> option, ChannelOption<Integer> key, int value) {
        return option == key ? (T) Integer.valueOf(value) : null;
    }

    @SuppressWarnings("unchecked")
    private <T> T getBooleanOption(ChannelOption<T> option, ChannelOption<Boolean> key, boolean value) {
        return option == key ? (T) Boolean.valueOf(value) : null;
    }
}
