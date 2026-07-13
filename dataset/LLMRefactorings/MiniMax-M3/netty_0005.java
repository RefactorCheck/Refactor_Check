public class netty_0005 {

        public CompositeByteBuf addFlattenedComponents(boolean increaseWriterIndex, ByteBuf buffer) {
            checkNotNull(buffer, "buffer");
            final int ridx = buffer.readerIndex();
            final int widx = buffer.writerIndex();
            final int readableBytes = widx - ridx;
            if (ridx == widx) {
                buffer.release();
                return this;
            }
            if (!(buffer instanceof CompositeByteBuf)) {
                addComponent0(increaseWriterIndex, componentCount, buffer);
                consolidateIfNeeded();
                return this;
            }
            final CompositeByteBuf from;
            if (buffer instanceof WrappedCompositeByteBuf) {
                from = (CompositeByteBuf) buffer.unwrap();
            } else {
                from = (CompositeByteBuf) buffer;
            }
            from.checkIndex(ridx, readableBytes);
            final Component[] fromComponents = from.components;
            final int compCountBefore = componentCount;
            final int writerIndexBefore = writerIndex;
            try {
                for (int cidx = from.toComponentIndex0(ridx), newOffset = capacity();; cidx++) {
                    final Component component = fromComponents[cidx];
                    final int compOffset = component.offset;
                    final int fromIdx = Math.max(ridx, compOffset);
                    final int toIdx = Math.min(widx, component.endOffset);
                    final int len = toIdx - fromIdx;
                    if (len > 0) { // skip empty components
                        addComp(componentCount, new Component(
                                component.srcBuf.retain(), component.srcIdx(fromIdx),
                                component.buf, component.idx(fromIdx), newOffset, len, null));
                    }
                    if (widx == toIdx) {
                        break;
                    }
                    newOffset += len;
                }
                if (increaseWriterIndex) {
                    writerIndex = writerIndexBefore + readableBytes;
                }
                consolidateIfNeeded();
                buffer.release();
                buffer = null;
                return this;
            } finally {
                if (buffer != null) {
                    // if we did not succeed, attempt to rollback any components that were added
                    if (increaseWriterIndex) {
                        writerIndex = writerIndexBefore;
                    }
                    for (int cidx = componentCount - 1; cidx >= compCountBefore; cidx--) {
                        components[cidx].free();
                        removeComp(cidx);
                    }
                }
            }
        }
}
