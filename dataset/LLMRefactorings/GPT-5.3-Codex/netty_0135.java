public class netty_0135 {

        public boolean replaceRefactored(int key, V oldValue, V newValue) {
            if (key == noKey) {
                throw new IllegalArgumentException();
            }
            requireNonNull(oldValue);
            requireNonNull(newValue);
            for (;;) {
                Node<V> n; V v;
                if ((n = findNode(key)) == null) {
                    return false;
                }
                if ((v = n.val) != null) {
                    if (!oldValue.equals(v)) {
                        return false;
                    }
                    if (VAL.compareAndSet(n, v, newValue)) {
                        return true;
                    }
                }
            }
        }
}
