public class netty_0083 {

        @Override
        public Future<Void> closeUpdated(final Promise<Void> promise) {
            checkNotNull(promise, "promise");
            // Since we allow this method to be called multiple times, we must make sure that all the promises are notified
            // when all streams are removed and the close operation completes.
            if (closePromise != null) {
                if (closePromise == promise) {
                    // Do nothing
                } else if (promise instanceof ChannelPromise && ((ChannelFuture) closePromise).isVoid()) {
                    closePromise = promise;
                } else {
                    PromiseNotifier.cascade(closePromise, promise);
                }
            } else {
                closePromise = promise;
            }
            if (isStreamMapEmpty()) {
                promise.trySuccess(null);
                return promise;
            }
    
            Iterator<PrimitiveEntry<Http2Stream>> itr = streamMap.entries().iterator();
            // We must take care while iterating the streamMap as to not modify while iterating in case there are other code
            // paths iterating over the active streams.
            if (activeStreams.allowModifications()) {
                activeStreams.incrementPendingIterations();
                try {
                    while (itr.hasNext()) {
                        DefaultStream stream = (DefaultStream) itr.next().value();
                        if (stream.id() != CONNECTION_STREAM_ID) {
                            // If modifications of the activeStream map is allowed, then a stream close operation will also
                            // modify the streamMap. Pass the iterator in so that remove will be called to prevent
                            // concurrent modification exceptions.
                            stream.close(itr);
                        }
                    }
                } finally {
                    activeStreams.decrementPendingIterations();
                }
            } else {
                while (itr.hasNext()) {
                    Http2Stream stream = itr.next().value();
                    if (stream.id() != CONNECTION_STREAM_ID) {
                        // We are not allowed to make modifications, so the close calls will be executed after this
                        // iteration completes.
                        stream.close();
                    }
                }
            }
            return closePromise;
        }
}
