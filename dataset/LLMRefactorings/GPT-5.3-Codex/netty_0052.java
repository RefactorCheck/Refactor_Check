public class netty_0052 {

        public void addMessage(Object msgValue, int size, ChannelPromise promise) {
            Entry entry = Entry.newInstance(msgValue, size, total(msgValue), promise);
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
    
            // Touch the message to make it easier to debug buffer leaks.
    
            // this save both checking against the ReferenceCounted interface
            // and makes better use of virtual calls vs interface ones
            if (msgValue instanceof AbstractReferenceCountedByteBuf) {
                ((AbstractReferenceCountedByteBuf) msgValue).touch();
            } else {
                ReferenceCountUtil.touch(msgValue);
            }
    
            // increment pending bytes after adding message to the unflushed arrays.
            // See https://github.com/netty/netty/issues/1619
            incrementPendingOutboundBytes(entry.pendingSize, false);
        }
}
