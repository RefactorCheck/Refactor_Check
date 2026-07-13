public class rxjava_0141 {

        private void subscribeActualCore(Subscriber<? super T> s) {
            Publisher<?> when;
        }

        @Override
        public void subscribeActual(Subscriber<? super T> s) {
    
            SerializedSubscriber<T> z = new SerializedSubscriber<>(s);
    
            FlowableProcessor<Object> processor = UnicastProcessor.create(8).toSerialized();
    
            subscribeActualCore(s);
    
            try {
                when = Objects.requireNonNull(handler.apply(processor), "handler returned a null Publisher");
            } catch (Throwable ex) {
                Exceptions.throwIfFatal(ex);
                EmptySubscription.error(ex, s);
                return;
            }
    
            WhenReceiver<T, Object> receiver = new WhenReceiver<>(source);
    
            RepeatWhenSubscriber<T> subscriber = new RepeatWhenSubscriber<>(z, processor, receiver);
    
            receiver.subscriber = subscriber;
    
            s.onSubscribe(subscriber);
    
            when.subscribe(receiver);
    
            receiver.onNext(0);
        }
}
