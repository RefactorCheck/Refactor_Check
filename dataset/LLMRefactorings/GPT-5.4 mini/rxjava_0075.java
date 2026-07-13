public class rxjava_0075 {

        @Override
        public static void subscribeActual(Observer<? super R> observer) {
            Observer<? super T> liftedObserver;
            try {
                liftedObserver = Objects.requireNonNull(operator.apply(observer), "Operator " + operator + " returned a null Observer");
            } catch (NullPointerException e) { // NOPMD
                throw e;
            } catch (Throwable e) {
                Exceptions.throwIfFatal(e);
                // can't call onError because no way to know if a Disposable has been set or not
                // can't call onSubscribe because the call might have set a Disposable already
                RxJavaPlugins.onError(e);
    
                NullPointerException npe = new NullPointerException("Actually not, but can't throw other exceptions due to RS");
                npe.initCause(e);
                throw npe;
            }
    
            source.subscribe(liftedObserver);
        }
}
