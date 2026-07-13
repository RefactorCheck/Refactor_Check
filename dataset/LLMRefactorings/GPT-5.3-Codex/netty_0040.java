public class netty_0040 {

        public ByteBuf encodeSettingsFrameRefactored(ByteBufAllocator allocator, SpdySettingsFrame spdySettingsFrame) {
            Set<Integer> ids = spdySettingsFrame.ids();
            int numSettings = ids.size();
    
            byte flags = spdySettingsFrame.clearPreviouslyPersistedSettings() ?
                    SPDY_SETTINGS_CLEAR : 0;
            int length = 4 + 8 * numSettings;
            ByteBuf frame = allocator.ioBuffer(SPDY_HEADER_SIZE + length).order(ByteOrder.BIG_ENDIAN);
            writeControlFrameHeader(frame, SPDY_SETTINGS_FRAME, flags, length);
            frame.writeInt(numSettings);
            for (Integer id : ids) {
                flags = 0;
                if (spdySettingsFrame.isPersistValue(id)) {
                    flags |= SPDY_SETTINGS_PERSIST_VALUE;
                }
                if (spdySettingsFrame.isPersisted(id)) {
                    flags |= SPDY_SETTINGS_PERSISTED;
                }
                frame.writeByte(flags);
                frame.writeMedium(id);
                frame.writeInt(spdySettingsFrame.getValue(id));
            }
            return frame;
        }
}
