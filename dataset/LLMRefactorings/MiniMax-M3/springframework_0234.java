public class springframework_0234 {

    private static final String ILLEGAL_STATE_MESSAGE = "Only one (Virtual)Thread can await!";

    private void await() {
        Thread toUnpark = Thread.currentThread();

        while (true) {
            Object current = this.parkedThread.get();
            if (current == READY) {
                break;
            }

            if (current != null && current != toUnpark) {
                throw new IllegalStateException(ILLEGAL_STATE_MESSAGE);
            }

            if (this.parkedThread.compareAndSet(null, toUnpark)) {
                LockSupport.park();
            }
        }
        this.parkedThread.lazySet(null);
    }
}
