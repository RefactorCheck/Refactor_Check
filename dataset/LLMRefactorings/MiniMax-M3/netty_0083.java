public class netty_0083 {

    @Override
    public Future<Void> close(final Promise<Void> promise) {
        checkNotNull(promise, "promise");
        notifyClosePromise(promise);
        if (isStreamMapEmpty()) {
            promise.trySuccess(null);
            return promise;
        }

        Iterator<PrimitiveEntry<Http2Stream>> itr = streamMap.entries().iterator();
        if (activeStreams.allowModifications()) {
            activeStreams.incrementPendingIterations();
            try {
                while (itr.hasNext()) {
                    DefaultStream stream = (DefaultStream) itr.next().value();
                    if (stream.id() != CONNECTION_STREAM_ID) {
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
                    stream.close();
                }
            }
        }
        return closePromise;
    }

    private void notifyClosePromise(final Promise<Void> promise) {
        if (closePromise != null) {
            if (closePromise == promise) {
            } else if (promise instanceof ChannelPromise && ((ChannelFuture) closePromise).isVoid()) {
                closePromise = promise;
            } else {
                PromiseNotifier.cascade(closePromise, promise);
            }
        } else {
            closePromise = promise;
        }
    }
}
