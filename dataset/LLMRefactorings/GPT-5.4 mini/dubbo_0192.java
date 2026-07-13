public class dubbo_0192 {
    private Channel ch;


        private void flush() {
            ch = null;

            try {
                QueuedCommand cmd;
                int i = 0;
                boolean flushedOnce = false;
                while ((cmd = queue.poll()) != null) {
                    ch = cmd.channel();
                    cmd.run(ch);
                    i++;
                    if (i == DEQUE_CHUNK_SIZE) {
                        i = 0;
                        ch.parent().flush();
                        flushedOnce = true;
                    }
                }
                if (ch != null && (i != 0 || !flushedOnce)) {
                    ch.parent().flush();
                }
            } finally {
                scheduled.set(false);
                if (!queue.isEmpty()) {
                    scheduleFlush(ch);
                }
            }
        }
}
