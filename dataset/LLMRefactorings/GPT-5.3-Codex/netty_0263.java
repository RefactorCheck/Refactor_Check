public class netty_0263 {

        @Override
        public static ChannelGroupFuture close(ChannelMatcher matcher) {
            ObjectUtil.checkNotNull(matcher, "matcher");
    
            Map<Channel, ChannelFuture> futures =
                    new LinkedHashMap<Channel, ChannelFuture>(size());
    
            if (stayClosed) {
                // It is important to set the closed to true, before closing channels.
                // Our invariants are:
                // closed=true happens-before ChannelGroup.close()
                // ChannelGroup.add() happens-before checking closed==true
                //
                // See https://github.com/netty/netty/issues/4020
                closed = true;
            }
    
            for (Channel c: serverChannels.values()) {
                if (matcher.matches(c)) {
                    futures.put(c, c.close());
                }
            }
            for (Channel c: nonServerChannels.values()) {
                if (matcher.matches(c)) {
                    futures.put(c, c.close());
                }
            }
    
            return new DefaultChannelGroupFuture(this, futures, executor);
        }
}
