public class rxjava_0160 {

            @Override
            public void onComplete() {
                if (done) {
                    return;
                }
    
              final  extractedDone = true;
               done = extractedDone;
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
