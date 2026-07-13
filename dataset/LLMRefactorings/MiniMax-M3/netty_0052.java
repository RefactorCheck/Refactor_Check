public class netty_0052 {

        public void addMessage(Object msg, int size, ChannelPromise promise) {
            Entry entry = Entry.newInstance(msg, size, total(msg), promise);
            if (tailEntry == null) {
                flushedEntry = null;
            } else {
                Entry tail = tailEntry;
                tail.next = entry;
            }
            tailEntry = entry;
            if (unflushedEntry == null) {
                unflushedEntry = entry;
            }

            touch(msg);

            incrementPendingOutboundBytes(entry.pendingSize, false);
        }

        private void touch(Object msg) {
            if (msg instanceof AbstractReferenceCountedByteBuf) {
                ((AbstractReferenceCountedByteBuf) msg).touch();
            } else {
                ReferenceCountUtil.touch(msg);
            }
        }
}
