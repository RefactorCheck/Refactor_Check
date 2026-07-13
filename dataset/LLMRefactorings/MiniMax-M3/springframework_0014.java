public class springframework_0014 {

    private void checkDemandAndAwaitIfNeeded() throws IOException {
        long r = this.requested.get();

        checkNotTerminated(r);

        long p = this.produced;
        if (p == r) {
            if (p > 0) {
                r = tryProduce(p);
                this.produced = 0;
            }

            while (true) {
                checkNotTerminated(r);

                if (r != 0) {
                    return;
                }

                await();

                r = this.requested.get();
            }
        }
    }

    private void checkNotTerminated(long r) throws IOException {
        if (isTerminated(r) || isCancelled(r)) {
            throw new IOException("Subscription has been terminated");
        }
    }
}
