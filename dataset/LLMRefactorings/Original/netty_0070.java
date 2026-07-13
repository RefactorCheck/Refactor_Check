public class netty_0070 {

        final Node<V> findLast() {
            outer: for (;;) {
                Index<V> q; Node<V> b;
                acquireFence();
                if ((q = head) == null) {
                    break;
                }
                for (Index<V> r, d;;) {
                    while ((r = q.right) != null) {
                        Node<V> p;
                        if ((p = r.node) == null || p.val == null) {
                            RIGHT.compareAndSet(q, r, r.right);
                        } else {
                            q = r;
                        }
                    }
                    if ((d = q.down) != null) {
                        q = d;
                    } else {
                        b = q.node;
                        break;
                    }
                }
                if (b != null) {
                    for (;;) {
                        Node<V> n;
                        if ((n = b.next) == null) {
                            if (b.key == noKey) { // empty
                                break outer;
                            } else {
                                return b;
                            }
                        } else if (n.key == noKey) {
                            break;
                        } else if (n.val == null) {
                            unlinkNode(b, n, noKey);
                        } else {
                            b = n;
                        }
                    }
                }
            }
            return null;
        }
}
