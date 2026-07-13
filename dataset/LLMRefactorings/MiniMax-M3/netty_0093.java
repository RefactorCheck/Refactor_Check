public class netty_0093 {

        @SuppressWarnings("unchecked")
        @Override
        public T newChannel() {
            switch (kind) {
                case ACCEPTOR:
                    return newAcceptorChannel();
                case CONNECTOR:
                    return newConnectorChannel();
                case RENDEZVOUS:
                    return newRendezvousChannel();
                default:
                    throw new IllegalStateException("wrong kind=" + kind);
            }
        }

        @SuppressWarnings("unchecked")
        private T newAcceptorChannel() {
            switch (type) {
                case DATAGRAM:
                    return (T) new NioUdtMessageAcceptorChannel();
                case STREAM:
                    return (T) new NioUdtByteAcceptorChannel();
                default:
                    throw new IllegalStateException("wrong type=" + type);
            }
        }

        @SuppressWarnings("unchecked")
        private T newConnectorChannel() {
            switch (type) {
                case DATAGRAM:
                    return (T) new NioUdtMessageConnectorChannel();
                case STREAM:
                    return (T) new NioUdtByteConnectorChannel();
                default:
                    throw new IllegalStateException("wrong type=" + type);
            }
        }

        @SuppressWarnings("unchecked")
        private T newRendezvousChannel() {
            switch (type) {
                case DATAGRAM:
                    return (T) new NioUdtMessageRendezvousChannel();
                case STREAM:
                    return (T) new NioUdtByteRendezvousChannel();
                default:
                    throw new IllegalStateException("wrong type=" + type);
            }
        }
}
