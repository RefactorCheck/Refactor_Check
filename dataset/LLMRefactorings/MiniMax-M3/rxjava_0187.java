public class rxjava_0187 {

    private static final String SUBSCRIBE_ACTUAL_FAILED = "subscribeActual failed";

        @SchedulerSupport(SchedulerSupport.NONE)
        @Override
        public final void subscribe(@NonNull SingleObserver<? super T> observer) {
            Objects.requireNonNull(observer, "observer is null");
    
            observer = RxJavaPlugins.onSubscribe(this, observer);
    
            Objects.requireNonNull(observer, "The RxJavaPlugins.onSubscribe hook returned a null SingleObserver. "
                    + "Please check the handler provided to RxJavaPlugins.setOnSingleSubscribe for invalid null returns. "
                    + "Further reading: https://github.com/ReactiveX/RxJava/wiki/Plugins");
    
            try {
                subscribeActual(observer);
            } catch (NullPointerException ex) {
                throw ex;
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                NullPointerException npe = new NullPointerException(SUBSCRIBE_ACTUAL_FAILED);
                npe.initCause(ex);
                throw npe;
            }
        }
}
