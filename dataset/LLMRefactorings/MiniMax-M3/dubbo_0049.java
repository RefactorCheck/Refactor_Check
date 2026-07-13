public class dubbo_0049 {

        protected void onSubscribe(final CallStreamObserver<?> subscription) {
            if (subscription != null && this.subscription == null && HAS_SUBSCRIPTION.compareAndSet(false, true)) {
                this.subscription = subscription;
                subscription.disableAutoFlowControl();
                setupOnReadyHandler(subscription);
                return;
            }
    
            throw new IllegalStateException(getClass().getSimpleName() + " supports only a single subscription");
        }

        private void setupOnReadyHandler(final CallStreamObserver<?> subscription) {
            if (onSubscribe != null) {
                subscription.setOnReadyHandler(() -> {
                    Consumer<CallStreamObserver<?>> callback = onSubscribe;
                    if (callback != null && subscription.isReady()) {
                        onSubscribe = null;
                        callback.accept(subscription);
                    }
                });
            }
        }
}
