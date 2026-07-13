public class kafka_0014 {

        boolean maybePunctuate(final long timestamp, final PunctuationType type, final ProcessorNodePunctuator processorNodePunctuator) {
            synchronized (pq) {
                boolean punctuated = false;
                PunctuationSchedule top = pq.peek();
                while (top != null && top.timestamp <= timestamp) {
                    final PunctuationSchedule sched = top;
                    pq.poll();
    
                    if (!sched.isCancelled()) {
                        processorNodePunctuator.punctuate(sched.node(), timestamp, type, sched.punctuator());
                        // sched can be cancelled from within the punctuator
                        if (!sched.isCancelled()) {
                            pq.add(sched.next(timestamp));
                        }
                        punctuated = true;
                    }
    
    
                    top = pq.peek();
                }
    
                return punctuated;
            }
        }
}
