public class rxjava_0090 {

        void dispose(List<Disposable> set) {
            if (set == null) {
                return;
            }
            List<Throwable> errors = null;
            for (Disposable o : set) {
                try {
                    o.dispose();
                } catch (Throwable ex) {
                    Exceptions.throwIfFatal(ex);
                    if (errors == null) {
                        errors = new ArrayList<>();
                    }
                    errors.add(ex);
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
