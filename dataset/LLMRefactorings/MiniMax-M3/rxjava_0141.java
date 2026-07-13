public class rxjava_0141 {

        @Override
        public void subscribeActual(Subscriber<? super T> s) {
    
            SerializedSubscriber<T> serializedSubscriber = new SerializedSubscriber<>(s);
    
            FlowableProcessor<Object> processor = UnicastProcessor.create(8).toSerialized();
    
            Publisher<?> when;
    
            try {
                when = Objects.requireNonNull(handler.apply(processor), "handler returned a null Publisher");
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                EmptySubscription.error(ex, s);
                return;
            }
    
            WhenReceiver<T, Object> receiver = new WhenReceiver<>(source);
    
            RepeatWhenSubscriber<T> subscriber = new RepeatWhenSubscriber<>(serializedSubscriber, processor, receiver);
    
            receiver.subscriber = subscriber;
    
            s.onSubscribe(subscriber);
    
            when.subscribe(receiver);
    
            receiver.onNext(0);
        }
}
