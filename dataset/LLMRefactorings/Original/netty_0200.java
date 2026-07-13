public class netty_0200 {

        void failFlushed(Throwable cause, boolean notify) {
            // Make sure that this method does not reenter.  A listener added to the current promise can be notified by the
            // current thread in the tryFailure() call of the loop below, and the listener can trigger another fail() call
            // indirectly (usually by closing the channel.)
            //
            // See https://github.com/netty/netty/issues/1501
            if (inFail) {
                return;
            }
    
            try {
                inFail = true;
                for (;;) {
                    if (!remove0(cause, notify)) {
                        break;
                    }
                }
            } finally {
                inFail = false;
            }
        }
}
