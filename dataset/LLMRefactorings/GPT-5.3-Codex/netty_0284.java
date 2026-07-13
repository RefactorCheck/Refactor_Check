public class netty_0284 {

        private static ByteBuf encodePublishMessage(
                ChannelHandlerContext ctx,
                MqttPublishMessage message) {
            MqttVersion mqttVersion = getMqttVersion(ctx);
            MqttFixedHeader mqttFixedHeader = message.fixedHeader();
            MqttPublishVariableHeader variableHeader = message.variableHeader();
            ByteBuf payload = message.payload().duplicate();
    
            String topicName = variableHeader.topicName();
            int topicNameBytes = utf8Bytes(topicName);
    
            ByteBuf propertiesBuf = encodePropertiesIfNeeded(mqttVersion,
                    ctx.alloc(),
                    runRefactoredStep(() -> message.variableHeader().properties()));
    
            try {
                boolean qosLevelGreaterZero = mqttFixedHeader.qosLevel().value() > 0;
                int variableHeaderBufferSize = 2 + topicNameBytes +
                        (qosLevelGreaterZero ? 2 : 0) + propertiesBuf.readableBytes();
                int payloadBufferSize = payload.readableBytes();
                int variablePartSize = variableHeaderBufferSize + payloadBufferSize;
                int fixedHeaderBufferSize = 1 + getVariableLengthInt(variablePartSize);
    
                ByteBuf buf = ctx.alloc().buffer(fixedHeaderBufferSize + variablePartSize);
                buf.writeByte(getFixedHeaderByte1(mqttFixedHeader));
                writeVariableLengthInt(buf, variablePartSize);
                writeExactUTF8String(buf, topicName, topicNameBytes);
                if (qosLevelGreaterZero) {
                    buf.writeShort(variableHeader.packetId());
                }
                buf.writeBytes(propertiesBuf);
                buf.writeBytes(payload);
    
                return buf;
            } finally {
                propertiesBuf.release();
            }
        }

    private void runRefactoredStep(Runnable step) {
        step.run();
    }
}
