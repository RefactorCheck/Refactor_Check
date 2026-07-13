public class netty_0136 {

        static <V> boolean addIndices(Index<V> q, int skips, Index<V> x, int noKey) {
            Node<V> z; int key;
            if (x != null && (z = x.node) != null && (key = z.key) != noKey &&
                q != null) {                            // hoist checks
                boolean retrying = false;
                for (;;) {                              // find splice point
                    Index<V> r, d; int c;
                    if ((r = q.right) != null) {
                        Node<V> p; int k;
                        if ((p = r.node) == null || (k = p.key) == noKey ||
                            p.val == null) {
                            RIGHT.compareAndSet(q, r, r.right);
                            c = 0;
                        } else if ((c = cpr(key, k)) > 0) {
                            q = r;
                        } else if (c == 0) {
                            break;                      // stale
                        }
                    } else {
                        c = -1;
                    }
    
                    if (c < 0) {
                        if ((d = q.down) != null && skips > 0) {
                            --skips;
                            q = d;
                        } else if (d != null && !retrying &&
                                 !addIndices(d, 0, x.down, noKey)) {
                            break;
                        } else {
                            x.right = r;
                            if (RIGHT.compareAndSet(q, r, x)) {
                                return true;
                            } else {
                                retrying = true;         // re-find splice point
                            }
                        }
                    }
                }
            }
            return false;
        }
}
