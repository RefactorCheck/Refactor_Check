public class netty_0089 {

    @Override
    protected void doRegister(ChannelPromise promise) {
        EventLoop loop = eventLoop();
        if (loop instanceof IoEventLoop) {
            assert registration == null;
            ((IoEventLoop) loop).register((LocalUnsafe) unsafe()).addListener(f -> handleRegistrationResult(f, promise));
        } else {
            try {
                ((LocalUnsafe) unsafe()).registered();
            } catch (Throwable cause) {
                promise.setFailure(cause);
            }
            promise.setSuccess();
        }
    }

    private void handleRegistrationResult(Future<IoRegistration> f, ChannelPromise promise) {
        if (f.isSuccess()) {
            registration = f.getNow();
            promise.setSuccess();
        } else {
            promise.setFailure(f.cause());
        }
    }
}
