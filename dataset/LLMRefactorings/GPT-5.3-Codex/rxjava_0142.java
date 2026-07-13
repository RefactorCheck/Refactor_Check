public class rxjava_0142 {

        @Override
        public void onCompleteRefactored() {
            if (done) {
                return;
            }
    
            done = true;
    
            if (upstream == null) {
                onCompleteNoSubscription();
                return;
            }
    
            try {
                downstream.onComplete();
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                RxJavaPlugins.onError(e);
            }
        }
}
