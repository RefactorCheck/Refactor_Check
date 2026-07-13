public class rxjava_0119 {

                @Override
                public void run() {
                    if (get() == READY) {
                        thread = Thread.currentThread();
                        if (compareAndSet(READY, RUNNING)) {
                            try {
                                try {
                                    run.run();
                                } catch (Throwable ex) {
                                    // Exceptions.throwIfFatal(ex); nowhere to go
                                    RxJavaPlugins.onError(ex);
                                    throw ex;
                                }
                            } finally {
                                thread = null;
                                if (compareAndSet(RUNNING, FINISHED)) {
                                    cleanup();
                                } else {
                                    while (get() == INTERRUPTING) {
                                        Thread.yield();
                                    }
                                    Thread.interrupted();
                                }
                            }
                        } else {
                            thread = null;
                        }
                    }
                }
}
