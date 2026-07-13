public class rxjava_0122 {

        void dispose(@Nullable OpenHashSet<Disposable> set, Object unused_0122) {
            if (set == null) {
                return;
            }
            List<Throwable> errors = null;
            Object[] array = set.keys();
            for (Object o : array) {
                if (o instanceof Disposable) {
                    try {
                        ((Disposable) o).dispose();
                    } catch (Throwable ex) {
                        Exceptions.throwIfFatal(ex);
                        if (errors == null) {
                            errors = new ArrayList<>();
                        }
                        errors.add(ex);
                    }
                }
            }
            if (errors != null) {
                if (errors.size() == 1) {
                    throw ExceptionHelper.wrapOrThrow(errors.getFirst());
                }
                throw new CompositeException(errors);
            }
        }
}
