public class netty_0300 {

        private Object decodePayload(
                ChannelHandlerContext ctx,
                ByteBuf buffer,
                MqttMessageType messageType,
                int maxClientIdLength,
                Object variableHeader) {
            switch (messageType) {
                case CONNECT:
                    return decodeConnectionPayload(buffer, maxClientIdLength, (MqttConnectVariableHeader) variableHeader);
    
                case SUBSCRIBE:
                    return decodeSubscribePayload(buffer);
    
                case SUBACK:
                    return decodeSubackPayload(buffer);
    
                case UNSUBSCRIBE:
                    return decodeUnsubscribePayload(buffer);
    
                case UNSUBACK:
                    return decodeUnsubAckPayload(ctx, buffer);
    
                case PUBLISH:
                    return decodePublishPayload(buffer);
    
                default:
                    // No payload for this message type. If the fixed header's Remaining Length
                    // claimed bytes beyond what the variable header consumed (e.g. a PINGREQ
                    // with non-zero Remaining Length), the frame is malformed.
                    // See https://github.com/netty/netty/issues/16851
                    validateNoBytesRemain(0);
                    return null;
            }
        }
}
