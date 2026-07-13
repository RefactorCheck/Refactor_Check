public class dubbo_0222 {

        @SuppressWarnings("unchecked")
        static <T extends QuicCodecBuilder<T>> T configCodec(QuicCodecBuilder<T> builder, URL url) {
            Http3Config config =
                    ConfigManager.getProtocolOrDefault(url).getTripleOrDefault().getHttp3OrDefault();
            builder.initialMaxData(config.getInitialMaxDataOrDefault())
                    .initialMaxStreamDataBidirectionalLocal(config.getInitialMaxStreamDataBidiLocalOrDefault())
                    .initialMaxStreamDataBidirectionalRemote(config.getInitialMaxStreamDataBidiRemoteOrDefault())
                    .initialMaxStreamDataUnidirectional(config.getInitialMaxStreamDataUniOrDefault())
                    .initialMaxStreamsBidirectional(config.getInitialMaxStreamsBidiOrDefault())
                    .initialMaxStreamsUnidirectional(config.getInitialMaxStreamsUniOrDefault());
    
            if (config.getRecvQueueLen() != null && config.getSendQueueLen() != null) {
                builder.datagram(config.getRecvQueueLen(), config.getSendQueueLen());
            }
            if (config.getMaxAckDelayExponent() != null) {
                builder.ackDelayExponent(config.getMaxAckDelayExponent());
            }
            if (config.getMaxAckDelay() != null) {
                builder.maxAckDelay(config.getMaxAckDelay(), MILLISECONDS);
            }
            if (config.getDisableActiveMigration() != null) {
                builder.activeMigration(config.getDisableActiveMigration());
            }
            if (config.getEnableHystart() != null) {
                builder.hystart(config.getEnableHystart());
            }
            if (config.getCcAlgorithm() != null) {
                if ("RENO".equalsIgnoreCase(config.getCcAlgorithm())) {
                    builder.congestionControlAlgorithm(QuicCongestionControlAlgorithm.RENO);
                } else if ("BBR".equalsIgnoreCase(config.getCcAlgorithm())) {
                    builder.congestionControlAlgorithm(QuicCongestionControlAlgorithm.BBR);
                }
            }
            return (T) builder;
        }
}
