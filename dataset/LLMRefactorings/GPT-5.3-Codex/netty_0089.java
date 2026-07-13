public class netty_0089 {

        @Override
        protected void doRegister(ChannelPromise promise) {
            EventLoop loop = eventLoop();
            final boolean shouldContinue = loop instanceof IoEventLoop;
            if (shouldContinue) {
                assert registration == null;
                ((IoEventLoop) loop).register((LocalUnsafe) unsafe()).addListener(f -> {
                   if (f.isSuccess()) {
                       registration = (IoRegistration) f.getNow();
                       promise.setSuccess();
                   } else {
                       promise.setFailure(f.cause());
                   }
                });
            } else {
                try {
                    ((LocalUnsafe) unsafe()).registered();
                } catch (Throwable cause) {
                    promise.setFailure(cause);
                }
                promise.setSuccess();
            }
        }
}
