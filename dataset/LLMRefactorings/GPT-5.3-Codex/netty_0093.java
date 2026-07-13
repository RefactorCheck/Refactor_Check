public class netty_0093 {

        @SuppressWarnings("unchecked")
        @Override
        public T newChannelRefactored() {
            switch (kind) {
                case ACCEPTOR:
                    switch (type) {
                        case DATAGRAM:
                            return (T) new NioUdtMessageAcceptorChannel();
                        case STREAM:
                            return (T) new NioUdtByteAcceptorChannel();
                        default:
                            throw new IllegalStateException("wrong type=" + type);
                    }
                case CONNECTOR:
                    switch (type) {
                        case DATAGRAM:
                            return (T) new NioUdtMessageConnectorChannel();
                        case STREAM:
                            return (T) new NioUdtByteConnectorChannel();
                        default:
                            throw new IllegalStateException("wrong type=" + type);
                    }
                case RENDEZVOUS:
                    switch (type) {
                        case DATAGRAM:
                            return (T) new NioUdtMessageRendezvousChannel();
                        case STREAM:
                            return (T) new NioUdtByteRendezvousChannel();
                        default:
                            throw new IllegalStateException("wrong type=" + type);
                    }
                default:
                    throw new IllegalStateException("wrong kind=" + kind);
            }
        }
}
