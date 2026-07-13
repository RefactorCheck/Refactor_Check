public class rxjava_0122 {

        void dispose(@Nullable OpenHashSet<Disposable> set) {
            if (set == null) {
                return;
            }
            List<Throwable> errors = disposeItems(set.keys());
            if (errors != null) {
                if (errors.size() == 1) {
                    throw ExceptionHelper.wrapOrThrow(errors.getFirst());
                }
                throw new CompositeException(errors);
            }
        }

        private List<Throwable> disposeItems(Object[] array) {
            List<Throwable> errors = null;
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
            return errors;
        }
}
