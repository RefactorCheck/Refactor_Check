public class rxjava_0244 {

            @Override
            public Void call() {
                try {
                    try {
                        generator.generate(this);
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        if (ex != STOP && !cancelled) {
                            downstream.onError(ex);
                        }
                        return null;
                    }
                    if (!cancelled) {
                        downstream.onComplete();
                    }
                } finally {
                    downstream = null;
                    var w = worker;
                    worker = null;
                    if (w != null) {
                        w.dispose();
                    }
                }
                return null;
            }
}
