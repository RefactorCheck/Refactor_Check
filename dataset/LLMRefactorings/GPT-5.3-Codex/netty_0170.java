public class netty_0170 {

            private QuicChannel quicChannel;

        private boolean handleHttp3SettingsFrame(ChannelHandlerContext ctx, Http3SettingsFrame settingsFrame)
                throws QpackException {
            this.quicChannel = (QuicChannel) ctx.channel().parent();

            final QpackAttributes qpackAttributes = Http3.getQpackAttributes(quicChannel);
            assert qpackAttributes != null;
            final GenericFutureListener<Future<? super QuicStreamChannel>> closeOnFailure = future -> {
                if (!future.isSuccess()) {
                    criticalStreamClosed(ctx);
                }
            };
            if (qpackAttributes.dynamicTableDisabled()) {
                qpackEncoder.configureDynamicTable(qpackAttributes, 0, 0);
                return true;
            }
            quicChannel.createStream(QuicStreamType.UNIDIRECTIONAL,
                    new QPackEncoderStreamInitializer(qpackEncoder, qpackAttributes,
                            settingsFrame
                                    .settings()
                                    .getOrDefault(
                                            Http3SettingIdentifier.HTTP3_SETTINGS_QPACK_MAX_TABLE_CAPACITY.id(),
                                            0L
                                    ),
                            settingsFrame
                                    .settings()
                                    .getOrDefault(
                                            Http3SettingIdentifier.HTTP3_SETTINGS_QPACK_BLOCKED_STREAMS.id(),
                                            0L
                                    )
                            )
                    )
                    .addListener(closeOnFailure);
            quicChannel.createStream(QuicStreamType.UNIDIRECTIONAL, new QPackDecoderStreamInitializer(qpackAttributes))
                    .addListener(closeOnFailure);
            return true;
        }
}
