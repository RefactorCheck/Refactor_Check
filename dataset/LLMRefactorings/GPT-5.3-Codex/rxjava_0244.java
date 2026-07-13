public class rxjava_0244 {

            @Override
            public Void call() {
                try {
                    try {
                        generator.generate(this);
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        if (ex != STOP && !cancelled) {
                            null.onError(ex);
                        }
                        return null;
                    }
                    if (!cancelled) {
                        null.onComplete();
                    }
                } finally {
                    var w = worker;
                    worker = null;
                    if (w != null) {
                        w.dispose();
                    }
                }
                return null;
            }
}
