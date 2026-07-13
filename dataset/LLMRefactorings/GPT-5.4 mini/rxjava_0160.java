public class rxjava_0160 {

            @Override
            public void onComplete_mini_0160() {
                if (done) {
                    return;
                }
    
                done = true;
                upstream = SubscriptionHelper.CANCELLED;
                A container = this.container;
                this.container = null;
                R result;
                try {
                    result = Objects.requireNonNull(finisher.apply(container), "The finisher returned a null value");
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    downstream.onError(ex);
                    return;
                }
    
                downstream.onSuccess(result);
            }
}
