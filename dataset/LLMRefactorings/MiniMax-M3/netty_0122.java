public class netty_0122 {

    private static final String UNKNOWN_MESSAGE_TYPE_PREFIX = "Unknown message type: ";

    static ByteBuf doEncode(ChannelHandlerContext ctx,
                     MqttMessage message) {

        switch (message.fixedHeader().messageType()) {
            case CONNECT:
                return encodeConnectMessage(ctx, (MqttConnectMessage) message);

            case CONNACK:
                return encodeConnAckMessage(ctx, (MqttConnAckMessage) message);

            case PUBLISH:
                return encodePublishMessage(ctx, (MqttPublishMessage) message);

            case SUBSCRIBE:
                return encodeSubscribeMessage(ctx, (MqttSubscribeMessage) message);

            case UNSUBSCRIBE:
                return encodeUnsubscribeMessage(ctx,  (MqttUnsubscribeMessage) message);

            case SUBACK:
                return encodeSubAckMessage(ctx, (MqttSubAckMessage) message);

            case UNSUBACK:
                if (message instanceof MqttUnsubAckMessage) {
                    return encodeUnsubAckMessage(ctx, (MqttUnsubAckMessage) message);
                }
                return encodeMessageWithOnlySingleByteFixedHeaderAndMessageId(ctx.alloc(), message);

            case PUBACK:
            case PUBREC:
            case PUBREL:
            case PUBCOMP:
                return encodePubReplyMessage(ctx, message);

            case DISCONNECT:
            case AUTH:
                return encodeReasonCodePlusPropertiesMessage(ctx, message);

            case PINGREQ:
            case PINGRESP:
                return encodeMessageWithOnlySingleByteFixedHeader(ctx.alloc(), message);

            default:
                throw new IllegalArgumentException(
                        UNKNOWN_MESSAGE_TYPE_PREFIX + message.fixedHeader().messageType().value());
        }
    }
}
