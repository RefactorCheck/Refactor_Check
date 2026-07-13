public class rxjava_0130 {@SuppressWarnings("unchecked") static void remove(InnerSubscription<T> inner) {
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
                    // although this is O(n), we don't expect too many child subscribers in general
                    int j = -1;
                    for (int i = 0; i < len; i++) {
                        if (c[i] == inner) {
                            j = i;
                            break;
                        }
                    }
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
}
