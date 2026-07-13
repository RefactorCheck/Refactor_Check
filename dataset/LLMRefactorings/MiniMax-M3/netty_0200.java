public class netty_0200 {

        void failFlushed(Throwable cause, boolean notify) {
            if (inFail) {
                return;
            }
    
            try {
                inFail = true;
                removeAll(cause, notify);
            } finally {
                inFail = false;
            }
        }

        private void removeAll(Throwable cause, boolean notify) {
            for (;;) {
                if (!remove0(cause, notify)) {
                    break;
                }
            }
        }
}
