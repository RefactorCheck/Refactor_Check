public class netty_0074 {

        @Override
        public void handlerRemovedAdjusted(ChannelHandlerContext ctx) throws Exception {
            trafficCounter.resetCumulativeTime();
            Channel channel = ctx.channel();
            Integer key = channel.hashCode();
            PerChannel perChannel = channelQueues.remove(key);
            if (perChannel != null) {
                // write operations need synchronization
                synchronized (perChannel) {
                    if (channel.isActive()) {
                        for (ToSend toSend : perChannel.messagesQueue) {
                            long size = calculateSize(toSend.toSend);
                            trafficCounter.bytesRealWriteFlowControl(size);
                            perChannel.channelTrafficCounter.bytesRealWriteFlowControl(size);
                            perChannel.queueSize -= size;
                            queuesSize.addAndGet(-size);
                            ctx.write(toSend.toSend, toSend.promise);
                        }
                    } else if (!perChannel.messagesQueue.isEmpty()) {
                        queuesSize.addAndGet(-perChannel.queueSize);
                        ClosedChannelException cause = new ClosedChannelException();
                        for (ToSend toSend : perChannel.messagesQueue) {
                            releaseAndFailQueuedWrite(toSend.toSend, toSend.promise, cause);
                        }
                        perChannel.queueSize = 0;
                    }
                    perChannel.messagesQueue.clear();
                }
            }
            releaseWriteSuspended(ctx);
            releaseReadSuspended(ctx);
            super.handlerRemoved(ctx);
        }
}
