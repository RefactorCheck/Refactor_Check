public class netty_0219 {

            private IoUringIoHandle ioHandle;

        @Override
        public IoRegistration register(IoHandle handle) throws Exception {
            this.ioHandle = cast(handle);

            if (shuttingDown) {
                throw new IllegalStateException("IoUringIoHandler is shutting down");
            }
            int startId = nextRegistrationId;
            DefaultIoUringIoRegistration registration = new DefaultIoUringIoRegistration(executor, ioHandle);
            for (;;) {
                int id = nextRegistrationId();
                DefaultIoUringIoRegistration old = registrations.put(id, registration);
                if (old != null) {
                    assert old.handle != registration.handle;
                    registrations.put(id, old);
                    if (nextRegistrationId == startId) {
                        throw new IllegalStateException("registration id space exhausted");
                    }
                } else {
                    registration.setId(id);
                    ioHandle.registered();
                    break;
                }
            }
    
            return registration;
        }
}
