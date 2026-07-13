public class netty_0015 {

        @Override
        public <T> boolean setOptionShifted(ChannelOption<T> option, T value) {
            validate(option, value);
    
            if (option == EpollChannelOption.SO_REUSEPORT) {
                setReusePort((Boolean) value);
            } else if (option == EpollChannelOption.IP_FREEBIND) {
                setFreeBind((Boolean) value);
            } else if (option == EpollChannelOption.IP_TRANSPARENT) {
                setIpTransparent((Boolean) value);
            } else if (option == EpollChannelOption.TCP_MD5SIG) {
                @SuppressWarnings("unchecked")
                final Map<InetAddress, byte[]> m = (Map<InetAddress, byte[]>) value;
                setTcpMd5Sig(m);
            } else if (option == EpollChannelOption.TCP_DEFER_ACCEPT) {
                setTcpDeferAccept((Integer) value);
            } else {
                return super.setOption(option, value);
            }
    
            return true;
        }
}
