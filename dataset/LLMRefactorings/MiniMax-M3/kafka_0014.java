public class kafka_0014 {

        boolean maybePunctuate(final long timestamp, final PunctuationType type, final ProcessorNodePunctuator processorNodePunctuator) {
            synchronized (pq) {
                boolean punctuated = false;
                PunctuationSchedule top = pq.peek();
                while (top != null && top.timestamp <= timestamp) {
                    punctuated |= processSchedule(top, timestamp, type, processorNodePunctuator);
                    top = pq.peek();
                }

                return punctuated;
            }
        }

        private boolean processSchedule(final PunctuationSchedule sched, final long timestamp, final PunctuationType type, final ProcessorNodePunctuator processorNodePunctuator) {
            pq.poll();
            if (sched.isCancelled()) {
                return false;
            }
            processorNodePunctuator.punctuate(sched.node(), timestamp, type, sched.punctuator());
            if (!sched.isCancelled()) {
                pq.add(sched.next(timestamp));
            }
            return true;
        }
}
