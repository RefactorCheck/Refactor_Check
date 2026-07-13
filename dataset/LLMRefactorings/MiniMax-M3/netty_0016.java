public class netty_0016 {
    public boolean spliceOut() throws Exception {
        assert ch.eventLoop().inEventLoop();
        try {
            int splicedOut = Native.splice(ch.pipeIn.intValue(), -1, ch.socket.intValue(), -1, len);
            len -= splicedOut;
            if (len == 0) {
                enableAutoRead();
                return true;
            }
            return false;
        } catch (IOException e) {
            enableAutoRead();
            throw e;
        }
    }

    private void enableAutoRead() {
        if (autoRead) {
            config().setAutoRead(true);
        }
    }
}
