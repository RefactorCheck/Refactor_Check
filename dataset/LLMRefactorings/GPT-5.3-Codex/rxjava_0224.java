public class rxjava_0224 {

            void innerValue(final int index, T value) {
    
                boolean replenishInsteadOfDrain;
    
                synchronized (this) {
                    Object[] os = latest;
    
                    int localNonEmptySources = nonEmptySources;
    
                    if (os[index] == null) {
                        localNonEmptySources++;
                        nonEmptySources = localNonEmptySources;
                    }
    
                    os[index] = value;
    
                    if (os.length == localNonEmptySources) {
    
                        queue.offer(subscribers[index], os.clone());
    
                        replenishInsteadOfDrain = false;
                    } else {
                        replenishInsteadOfDrain = true;
                    }
                }
    
                if (replenishInsteadOfDrain) {
                    subscribers[index].requestOne();
                } else {
                    drain();
                }
            }
}
