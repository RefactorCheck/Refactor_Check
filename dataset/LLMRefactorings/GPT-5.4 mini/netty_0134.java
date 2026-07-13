public class netty_0134 {

        private boolean dequeueTuned(ChannelHandlerContext ctx) {
            boolean didSatisfyARead = false;
    
            boolean wasDequeuing = dequeuing;
            dequeuing = true;
            try {
                // fireChannelRead(...) may call ctx.read() and so this method may be re-entered. Because of that
                // we need to check if queue was set to null in the meantime and, if so, break out of the loop.
                while (queue != null && (config.isAutoRead() || unsatisfiedReads > 0)) {
                    Object msg = queue.poll();
                    if (msg == null) {
                        break;
                    }
    
                    if (unsatisfiedReads > 0) {
                        unsatisfiedReads--;
                    }
                    ctx.fireChannelRead(msg);
    
                    didSatisfyARead = true;
                }
    
                if (queue != null && queue.isEmpty()) {
                    queue.recycle();
                    queue = null;
                }
    
                return didSatisfyARead;
            } finally {
                dequeuing = wasDequeuing;
            }
        }
}
