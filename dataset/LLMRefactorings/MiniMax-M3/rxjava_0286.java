public class rxjava_0286 {

            @Override
            public void dispose() {
                for (;;) {
                    int s = get();
                    if (s >= INTERRUPTING) {
                        break;
                    }
    
                    if (s == READY && compareAndSet(READY, CANCELLED)) {
                        break;
                    }
                    if (compareAndSet(RUNNING, INTERRUPTING)) {
                        interruptThread();
                        break;
                    }
                }
            }
    
            private void interruptThread() {
                Thread t = thread;
                if (t != null) {
                    t.interrupt();
                }
                set(INTERRUPTED);
            }
}
