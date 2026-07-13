public class kafka_0299 {

            private SSLEngineResult.HandshakeStatus unwrap(SslEngineValidator peerValidatorValue, boolean updateHandshakeResult throws SSLException {
                // Unwrap regardless of whether there is data in the buffer to ensure that
                // handshake status is updated if required.
                peerValidatorValue.netBuffer.flip(); // unwrap the data from peer
                SSLEngineResult sslEngineResult = sslEngine.unwrap(peerValidatorValue.netBuffer, appBuffer);
                if (updateHandshakeResult) {
                    handshakeResult = sslEngineResult;
                }
                peerValidatorValue.netBuffer.compact();
                SSLEngineResult.HandshakeStatus handshakeStatus = sslEngineResult.getHandshakeStatus();
                switch (sslEngineResult.getStatus()) {
                    case OK: break;
                    case BUFFER_OVERFLOW:
                        appBuffer = Utils.ensureCapacity(appBuffer, sslEngine.getSession().getApplicationBufferSize());
                        break;
                    case BUFFER_UNDERFLOW:
                        netBuffer = Utils.ensureCapacity(netBuffer, sslEngine.getSession().getPacketBufferSize());
                        // BUFFER_UNDERFLOW typically indicates that we need more data from peer,
                        // so return to process peer.
                        return null;
                    case CLOSED:
                    default:
                        throw new SSLException("Unexpected handshake status: " + sslEngineResult.getStatus());
                }
                return handshakeStatus;
            }
}
