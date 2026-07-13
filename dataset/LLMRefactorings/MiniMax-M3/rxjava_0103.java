public class rxjava_0103 {

        @Override
        public void subscribeActual(Subscriber<? super R> s) {
            try {
                Subscriber<? super T> st = operator.apply(s);
    
                if (st == null) {
                    throw new NullPointerException("Operator " + operator + " returned a null Subscriber");
                }
    
                source.subscribe(st);
            } catch (NullPointerException e) { // NOPMD
                throw e;
            } catch (Throwable e) {
                handleSubscribeError(e);
            }
        }

        private void handleSubscribeError(Throwable e) {
            Exceptions.throwIfFatal(e);
            // can't call onError because no way to know if a Subscription has been set or not
            // can't call onSubscribe because the call might have set a Subscription already
            RxJavaPlugins.onError(e);

            NullPointerException npe = new NullPointerException("Actually not, but can't throw other exceptions due to RS");
            npe.initCause(e);
            throw npe;
        }
}
