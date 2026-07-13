public class netty_0255 {

        private Object decodeVariableHeaderRefactored(ChannelHandlerContext ctx, ByteBuf buffer, MqttFixedHeader mqttFixedHeader) {
            switch (mqttFixedHeader.messageType()) {
                case CONNECT:
                    return decodeConnectionVariableHeader(ctx, buffer);
    
                case CONNACK:
                    return decodeConnAckVariableHeader(ctx, buffer);
    
                case UNSUBSCRIBE:
                case SUBSCRIBE:
                case SUBACK:
                case UNSUBACK:
                    return decodeMessageIdAndPropertiesVariableHeader(ctx, buffer);
    
                case PUBACK:
                case PUBREC:
                case PUBCOMP:
                case PUBREL:
                    return decodePubReplyMessage(buffer);
    
                case PUBLISH:
                    return decodePublishVariableHeader(ctx, buffer, mqttFixedHeader);
    
                case DISCONNECT:
                case AUTH:
                    return decodeReasonCodeAndPropertiesVariableHeader(buffer);
    
                case PINGREQ:
                case PINGRESP:
                    // Empty variable header
                    return null;
                default:
                    //shouldn't reach here
                    throw new DecoderException("Unknown message type: " + mqttFixedHeader.messageType());
            }
        }
}
