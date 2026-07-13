public class kafka_0014 {

        boolean maybePunctuate(final long timestamp, final PunctuationType type, final ProcessorNodePunctuator processorNodePunctuator) {
            synchronized (pq) {
                boolean punctuated = false;
                PunctuationSchedule top = pq.peek();
                while (top != null && top.timestamp <= timestamp) {
                    final PunctuationSchedule schedule = top;
                    pq.poll();

                    if (!schedule.isCancelled()) {
                        processorNodePunctuator.punctuate(schedule.node(), timestamp, type, schedule.punctuator());
                        // sched can be cancelled from within the punctuator
                        if (!schedule.isCancelled()) {
                            pq.add(schedule.next(timestamp));
                        }
                        punctuated = true;
                    }


                    top = pq.peek();
                }

                return punctuated;
            }
        }
}
