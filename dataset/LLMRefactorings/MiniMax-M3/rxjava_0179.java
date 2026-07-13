public class rxjava_0179 {

    private static final String OBSERVER_NULL_MESSAGE = "observer is null";
    private static final String RXJAVA_PLUGINS_NULL_MESSAGE = "The RxJavaPlugins.onSubscribe hook returned a null CompletableObserver. "
            + "Please check the handler provided to RxJavaPlugins.setOnCompletableSubscribe for invalid null returns. "
            + "Further reading: https://github.com/ReactiveX/RxJava/wiki/Plugins";

        @SchedulerSupport(SchedulerSupport.NONE)
        @Override
        public final void subscribe(@NonNull CompletableObserver observer) {
            Objects.requireNonNull(observer, OBSERVER_NULL_MESSAGE);
            try {
    
                observer = RxJavaPlugins.onSubscribe(this, observer);
    
                Objects.requireNonNull(observer, RXJAVA_PLUGINS_NULL_MESSAGE);
    
                subscribeActual(observer);
            } catch (NullPointerException ex) { // NOPMD
                throw ex;
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                RxJavaPlugins.onError(ex);
                throw toNpe(ex);
            }
        }
}
