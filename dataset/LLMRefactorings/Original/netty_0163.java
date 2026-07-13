public class netty_0163 {

        final ChannelFuture initAndRegister() {
            Channel channel = null;
            try {
                channel = channelFactory.newChannel();
                init(channel);
            } catch (Throwable t) {
                if (channel != null) {
                    // channel can be null if newChannel crashed (eg SocketException("too many open files"))
                    channel.unsafe().closeForcibly();
                    // as the Channel is not registered yet we need to force the usage of the GlobalEventExecutor
                    return new DefaultChannelPromise(channel, GlobalEventExecutor.INSTANCE).setFailure(t);
                }
                // as the Channel is not registered yet we need to force the usage of the GlobalEventExecutor
                return new DefaultChannelPromise(new FailedChannel(), GlobalEventExecutor.INSTANCE).setFailure(t);
            }
    
            final ChannelFuture regFuture = config().group().register(channel);
            if (regFuture.cause() != null) {
                if (channel.isRegistered()) {
                    channel.close();
                } else {
                    channel.unsafe().closeForcibly();
                }
            }
    
            // If we are here and the promise is not failed, it's one of the following cases:
            // 1) If we attempted registration from the event loop, the registration has been completed at this point.
            //    i.e. It's safe to attempt bind() or connect() now because the channel has been registered.
            // 2) If we attempted registration from the other thread, the registration request has been successfully
            //    added to the event loop's task queue for later execution.
            //    i.e. It's safe to attempt bind() or connect() now:
            //         because bind() or connect() will be executed *after* the scheduled registration task is executed
            //         because register(), bind(), and connect() are all bound to the same thread.
    
            return regFuture;
        }
}
