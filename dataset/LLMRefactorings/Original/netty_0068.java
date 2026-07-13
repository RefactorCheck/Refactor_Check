public class netty_0068 {

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            arena.lock();
            try {
                if (head == null) {
                    return "none";
                }
    
                for (PoolChunk<T> cur = head;;) {
                    buf.append(cur);
                    cur = cur.next;
                    if (cur == null) {
                        break;
                    }
                    buf.append(StringUtil.NEWLINE);
                }
            } finally {
                arena.unlock();
            }
            return buf.toString();
        }
}
