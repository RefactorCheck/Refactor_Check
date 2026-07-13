public class rxjava_0292 {

    @Setup
    public void setup() {

        Scheduler s = Schedulers.single();

        Scheduler s2 = new SingleScheduler();

        Callable<Integer> c = () -> 1;

        setupFlowable(s, s2, c);

        setupObservable(s, s2, c);

        setupSingle(s, s2, c);

        setupCompletable(s, s2, c);

        setupMaybe(s, s2, c);
    }

    private void setupFlowable(Scheduler s, Scheduler s2, Callable<Integer> c) {
        subscribeOnFlowable = Flowable.fromCallable(c).subscribeOn(s);

        observeOnFlowable = Flowable.fromCallable(c).observeOn(s);

        pipelineFlowable = Flowable.fromCallable(c).subscribeOn(s).observeOn(s2);
    }

    private void setupObservable(Scheduler s, Scheduler s2, Callable<Integer> c) {
        subscribeOnObservable = Observable.fromCallable(c).subscribeOn(s);

        observeOnObservable = Observable.fromCallable(c).observeOn(s);

        pipelineObservable = Observable.fromCallable(c).subscribeOn(s).observeOn(s2);
    }

    private void setupSingle(Scheduler s, Scheduler s2, Callable<Integer> c) {
        observeOnSingle = Single.fromCallable(c).observeOn(s);

        subscribeOnSingle = Single.fromCallable(c).subscribeOn(s);

        pipelineSingle = Single.fromCallable(c).subscribeOn(s).observeOn(s2);
    }

    private void setupCompletable(Scheduler s, Scheduler s2, Callable<Integer> c) {
        observeOnCompletable = Completable.fromCallable(c).observeOn(s);

        subscribeOnCompletable = Completable.fromCallable(c).subscribeOn(s);

        pipelineCompletable = Completable.fromCallable(c).subscribeOn(s).observeOn(s2);
    }

    private void setupMaybe(Scheduler s, Scheduler s2, Callable<Integer> c) {
        observeOnMaybe = Maybe.fromCallable(c).observeOn(s);

        subscribeOnMaybe = Maybe.fromCallable(c).subscribeOn(s);

        pipelineMaybe = Maybe.fromCallable(c).subscribeOn(s).observeOn(s2);
    }
}
