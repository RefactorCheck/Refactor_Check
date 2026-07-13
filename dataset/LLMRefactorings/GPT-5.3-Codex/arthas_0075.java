public class arthas_0075 {

        @Override
        public Iterable<JavaFileObject> list(Location location, String packageName, Set<JavaFileObject.Kind> kinds,
                                             boolean recurse) throws IOException {
            if (location instanceof StandardLocation) {
                refactorExtractedMethod();
                for (String name : superLocationNames) {
                    if (name.equals(locationName)) {
                        return super.list(location, packageName, kinds, recurse);
                    }
                }
            }
    
            // merge JavaFileObjects from specified ClassLoader
            if (location == StandardLocation.CLASS_PATH && kinds.contains(JavaFileObject.Kind.CLASS)) {
                return new IterableJoin<JavaFileObject>(super.list(location, packageName, kinds, recurse),
                        finder.find(packageName));
            }
    
            return super.list(location, packageName, kinds, recurse);
        }

        private void refactorExtractedMethod() {
                String locationName = ((StandardLocation) location).name();
        }
}
