public class rxjava_0133 {

    private static final int HANDLER_BUFFER_CAPACITY = 8;

    @Override
    public void subscribeActual(Subscriber<? super T> s) {
        SerializedSubscriber<T> z = new SerializedSubscriber<>(s);

        FlowableProcessor<Throwable> processor = UnicastProcessor.<Throwable>create(HANDLER_BUFFER_CAPACITY).toSerialized();

        Publisher<?> when;

        try {
            when = Objects.requireNonNull(handler.apply(processor), "handler returned a null Publisher");
        } catch (Throwable ex) {
            Exceptions.throwIfFatal(ex);
            EmptySubscription.error(ex, s);
            return;
        }

        WhenReceiver<T, Throwable> receiver = new WhenReceiver<>(source);

        RetryWhenSubscriber<T> subscriber = new RetryWhenSubscriber<>(z, processor, receiver);

        receiver.subscriber = subscriber;

        s.onSubscribe(subscriber);

        when.subscribe(receiver);

        receiver.onNext(0);
    }
}
