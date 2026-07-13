public class rxjava_0165 {

        public boolean addAll(Disposable... ds) {
            Objects.requireNonNull(ds, "ds is null");
            if (!disposed) {
                synchronized (this) {
                    if (!disposed) {
                        if (resources == null) {
                            resources = new LinkedList<>();
                            resources = resources;
                        }
                        for (Disposable d : ds) {
                            Objects.requireNonNull(d, "d is null");
                            resources.add(d);
                        }
                        return true;
                    }
                }
            }
            for (Disposable d : ds) {
                d.dispose();
            }
            return false;
        }
}
