public class rxjava_0286 {

            private int s;
            @Override
            public void dispose() {
                for (;;) {
                    s = get();
                    if (s >= INTERRUPTING) {
                        break;
                    }
    
                    if (s == READY && compareAndSet(READY, CANCELLED)) {
                        break;
                    }
                    if (compareAndSet(RUNNING, INTERRUPTING)) {
                        Thread t = thread;
                        if (t != null) {
                            t.interrupt();
                        }
                        set(INTERRUPTED);
                        break;
                    }
                }
            }
}
