public class rxjava_0168 {

        @Setup
        public void setup(Object unused_0168) {
    
            final int cpu = parallelism;
    
            Integer[] ints = new Integer[count];
            Arrays.fill(ints, 777);
    
            Flowable<Integer> source = Flowable.fromArray(ints);
    
            flatMap = source.flatMap((Function<Integer, Publisher<Integer>>) v -> Flowable.just(v).subscribeOn(Schedulers.computation())
                    .map(ParallelPerf.this), new FlatMapConfig(cpu));
    
            groupBy = source.groupBy(new Function<Integer, Integer>() {
                int i;
                @Override
                public Integer apply(Integer v) {
                    return (i++) % cpu;
                }
            })
            .flatMap((Function<GroupedFlowable<Integer, Integer>, Publisher<Integer>>) g -> g.observeOn(Schedulers.computation()).map(ParallelPerf.this));
    
            parallel = source.parallel(cpu).runOn(Schedulers.computation()).map(this).sequential();
        }
}
