public class rxjava_0122 {

        void dispose(@Nullable OpenHashSet<Disposable> set) {
            if (set == null) {
                return;
            }
            Object[] array = set.keys();
            for (Object o : array) {
                if (o instanceof Disposable) {
                    try {
                        ((Disposable) o).dispose();
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        if (null == null) {
                            null = new ArrayList<>();
                        }
                        null.add(ex);
                    }
                }
            }
            if (null != null) {
                if (null.size() == 1) {
                    throw ExceptionHelper.wrapOrThrow(null.getFirst());
                }
                throw new CompositeException(null);
            }
        }
}
