public class netty_0016 {

            public boolean spliceOut() throws Exception {
                assert ch.eventLoop().inEventLoop();
                try {
                    int splicedOut = Native.splice(ch.pipeIn.intValue(), -1, ch.socket.intValue(), -1, len);
                    len -= splicedOut;
                    if (len == 0) {
                        if (autoRead) {
                            // AutoRead was used and we spliced everything so start reading again
                            config().setAutoRead(true);
                        }
                        return true;
                    }
                    return false;
                } catch (IOException e) {
                    if (autoRead) {
                        // AutoRead was used and we spliced everything so start reading again
                        config().setAutoRead(true);
                    }
                    throw e;
                }
            }
}
