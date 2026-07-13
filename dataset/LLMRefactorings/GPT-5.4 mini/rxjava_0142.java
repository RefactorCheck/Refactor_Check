public class rxjava_0142 {

        @Override
        public void onComplete(Object unused_0142) {
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
