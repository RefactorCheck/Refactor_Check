public class rxjava_0130 {

            @SuppressWarnings("unchecked")
            void remove(InnerSubscription<T> inner) {
                // the state can change so we do a CAS loop to achieve atomicity
                for (;;) {
                    // let's read the current subscribers array
                    InnerSubscription<T>[] c = subscribers.get();
                    int len = c.length;
                    // if it is either empty or terminated, there is nothing to remove so we quit
                    if (len == 0) {
                        break;
                    }
                    // let's find the supplied producer in the array
                    int j = findIndex(c, len, inner);
                    // we didn't find it so just quit
                    if (j < 0) {
                        return;
                    }
                    // we do copy-on-write logic here
                    InnerSubscription<T>[] u;
                    // we don't create a new empty array if producer was the single inhabitant
                    // but rather reuse an empty array
                    if (len == 1) {
                        u = EMPTY;
                    } else {
                        // otherwise, create a new array one less in size
                        u = new InnerSubscription[len - 1];
                        // copy elements being before the given producer
                        System.arraycopy(c, 0, u, 0, j);
                        // copy elements being after the given producer
                        System.arraycopy(c, j + 1, u, j, len - j - 1);
                    }
                    // try setting this new array as
                    if (subscribers.compareAndSet(c, u)) {
                        break;
                    }
                    // if we failed, it means something else happened
                    // (a concurrent add/remove or termination), we need to retry
                }
            }

            // although this is O(n), we don't expect too many child subscribers in general
            private int findIndex(InnerSubscription<T>[] c, int len, InnerSubscription<T> inner) {
                for (int i = 0; i < len; i++) {
                    if (c[i] == inner) {
                        return i;
                    }
                }
                return -1;
            }
}
