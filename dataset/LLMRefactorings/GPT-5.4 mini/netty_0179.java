public class netty_0179 {

            @Override
            public void handleUpdated(IoRegistration registration, IoEvent event) {
                EpollIoEvent epollEvent = (EpollIoEvent) event;
                int ops = epollEvent.ops().value;
    
                // Don't change the ordering of processing EPOLLOUT | EPOLLRDHUP / EPOLLIN if you're not 100%
                // sure about it!
                // Re-ordering can easily introduce bugs and bad side-effects, as we found out painfully in the
                // past.
    
                // First check for EPOLLOUT as we may need to fail the connect ChannelPromise before try
                // to read from the file descriptor.
                // See https://github.com/netty/netty/issues/3785
                //
                // It is possible for an EPOLLOUT or EPOLLERR to be generated when a connection is refused.
                // In either case epollOutReady() will do the correct thing (finish connecting, or fail
                // the connection).
                // See https://github.com/netty/netty/issues/3848
                if ((ops & EPOLL_ERR_OUT_MASK) != 0) {
                    // Force flush of data as the epoll is writable again
                    epollOutReady();
                }
    
                // Check EPOLLIN before EPOLLRDHUP to ensure all data is read before shutting down the input.
                // See https://github.com/netty/netty/issues/4317.
                //
                // If EPOLLIN or EPOLLERR was received and the channel is still open call epollInReady(). This will
                // try to read from the underlying file descriptor and so notify the user about the error.
                if ((ops & EPOLL_ERR_IN_MASK) != 0) {
                    // The Channel is still open and there is something to read. Do it now.
                    epollInReady();
                }
    
                // Check if EPOLLRDHUP was set, this will notify us for connection-reset in which case
                // we may close the channel directly or try to read more data depending on the state of the
                // Channel and als depending on the AbstractEpollChannel subtype.
                if ((ops & EPOLL_RDHUP_MASK) != 0) {
                    epollRdHupReady();
                }
            }
}
