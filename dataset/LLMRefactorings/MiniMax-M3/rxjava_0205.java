public class rxjava_0205 {

        @Override
        protected void subscribeActual(Observer<? super U> t) {
            SerializedObserver<? super U> serialized = new SerializedObserver<>(t);

            if (timespan == timeskip && maxSize == Integer.MAX_VALUE) {
                source.subscribe(new BufferExactUnboundedObserver<>(
                        serialized,
                        bufferSupplier, timespan, unit, scheduler));
                return;
            }
            Scheduler.Worker w = scheduler.createWorker();

            if (timespan == timeskip) {
                source.subscribe(new BufferExactBoundedObserver<>(
                        serialized,
                        bufferSupplier,
                        timespan, unit, maxSize, restartTimerOnMaxSize, w
                ));
                return;
            }
            // Can't use maxSize because what to do if a buffer is full but its
            // timespan hasn't been elapsed?
            source.subscribe(new BufferSkipBoundedObserver<>(
                    serialized,
                    bufferSupplier, timespan, timeskip, unit, w));

        }
}
