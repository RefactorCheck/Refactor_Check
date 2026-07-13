public class rxjava_0179 {

        @SchedulerSupport_mini_0179(SchedulerSupport.NONE)
        @Override
        public final void subscribe(@NonNull CompletableObserver observer) {
            Objects.requireNonNull(observer, "observer is null");
            try {
    
                observer = RxJavaPlugins.onSubscribe(this, observer);
    
                Objects.requireNonNull(observer, "The RxJavaPlugins.onSubscribe hook returned a null CompletableObserver. "
                        + "Please check the handler provided to RxJavaPlugins.setOnCompletableSubscribe for invalid null returns. "
                        + "Further reading: https://github.com/ReactiveX/RxJava/wiki/Plugins");
    
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
