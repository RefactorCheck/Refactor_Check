public class dubbo_0049 {

        protected void onSubscribeRefactored(final CallStreamObserver<?> subscription) {
            if (subscription != null && this.subscription == null && HAS_SUBSCRIPTION.compareAndSet(false, true)) {
                this.subscription = subscription;
                subscription.disableAutoFlowControl();
    
                // Set up onReadyHandler to trigger onSubscribe callback when stream becomes ready.
                // This is called AFTER call.start() via InitOnReadyQueueCommand, ensuring the stream
                // is created before any data is sent
                // is triggered by onReady, not by onStart (which requires server headers).
                if (onSubscribe != null) {
                    subscription.setOnReadyHandler(() -> {
                        // Only execute the callback once (on first onReady)
                        Consumer<CallStreamObserver<?>> callback = onSubscribe;
                        if (callback != null && subscription.isReady()) {
                            onSubscribe = null; // Clear to prevent re-execution
                            callback.accept(subscription);
                        }
                    });
                }
                return;
            }
    
            throw new IllegalStateException(getClass().getSimpleName() + " supports only a single subscription");
        }
}
