public class netty_0263 {

        @Override
        public ChannelGroupFuture close(ChannelMatcher matcher) {
            ObjectUtil.checkNotNull(matcher, "matcher");
    
            Map<Channel, ChannelFuture> futures =
                    new LinkedHashMap<Channel, ChannelFuture>(size());
    
            if (stayClosed) {
                closed = true;
            }
    
            addCloseFutures(serverChannels.values(), matcher, futures);
            addCloseFutures(nonServerChannels.values(), matcher, futures);
    
            return new DefaultChannelGroupFuture(this, futures, executor);
        }
        
        private void addCloseFutures(Iterable<Channel> channels, ChannelMatcher matcher, 
                                     Map<Channel, ChannelFuture> futures) {
            for (Channel c: channels) {
                if (matcher.matches(c)) {
                    futures.put(c, c.close());
                }
            }
        }
}
