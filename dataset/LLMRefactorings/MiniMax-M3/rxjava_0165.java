public class rxjava_0165 {

        public boolean addAll(Disposable... ds) {
            Objects.requireNonNull(ds, "ds is null");
            if (!disposed) {
                synchronized (this) {
                    if (!disposed) {
                        List<Disposable> set = resources;
                        if (set == null) {
                            set = new LinkedList<>();
                            resources = set;
                        }
                        for (Disposable d : ds) {
                            Objects.requireNonNull(d, "d is null");
                            set.add(d);
                        }
                        return true;
                    }
                }
            }
            disposeAll(ds);
            return false;
        }

        private void disposeAll(Disposable... ds) {
            for (Disposable d : ds) {
                d.dispose();
            }
        }
}
