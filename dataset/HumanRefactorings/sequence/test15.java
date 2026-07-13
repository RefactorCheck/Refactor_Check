class test15 {
    private boolean notified = false;

    public void callerMethod() throws InterruptedException {
        // Some logic here
        synchronized (this) {
            markNotified();
            notify();
        }
    }

    private void markNotified() {
        notified = true;
    }
}
