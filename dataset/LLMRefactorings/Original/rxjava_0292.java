public class rxjava_0292 {

        @Setup
        public void setup() {
    
            Scheduler s = Schedulers.single();
    
            Scheduler s2 = new SingleScheduler();
    
            Callable<Integer> c = () -> 1;
    
            subscribeOnFlowable = Flowable.fromCallable(c).subscribeOn(s);
    
            observeOnFlowable = Flowable.fromCallable(c).observeOn(s);
    
            pipelineFlowable = Flowable.fromCallable(c).subscribeOn(s).observeOn(s2);
    
            // ----
    
            subscribeOnObservable = Observable.fromCallable(c).subscribeOn(s);
    
            observeOnObservable = Observable.fromCallable(c).observeOn(s);
    
            pipelineObservable = Observable.fromCallable(c).subscribeOn(s).observeOn(s2);
    
            // ----
    
            observeOnSingle = Single.fromCallable(c).observeOn(s);
    
            subscribeOnSingle = Single.fromCallable(c).subscribeOn(s);
    
            pipelineSingle = Single.fromCallable(c).subscribeOn(s).observeOn(s2);
    
            // ----
    
            observeOnCompletable = Completable.fromCallable(c).observeOn(s);
    
            subscribeOnCompletable = Completable.fromCallable(c).subscribeOn(s);
    
            pipelineCompletable = Completable.fromCallable(c).subscribeOn(s).observeOn(s2);
    
            // ----
    
            observeOnMaybe = Maybe.fromCallable(c).observeOn(s);
    
            subscribeOnMaybe = Maybe.fromCallable(c).subscribeOn(s);
    
            pipelineMaybe = Maybe.fromCallable(c).subscribeOn(s).observeOn(s2);
        }
}
