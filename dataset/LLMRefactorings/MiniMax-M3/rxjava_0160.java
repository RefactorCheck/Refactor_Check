public class rxjava_0160 {

            @Override
            public void onComplete() {
                if (done) {
                    return;
                }
    
                done = true;
                upstream = SubscriptionHelper.CANCELLED;
                A container = this.container;
                this.container = null;
                R result = applyFinisher(container);
                if (result == null) {
                    return;
                }
    
                downstream.onSuccess(result);
            }
    
            private R applyFinisher(A container) {
                try {
                    return Objects.requireNonNull(finisher.apply(container), "The finisher returned a null value");
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    downstream.onError(ex);
                    return null;
                }
            }
}
