public class netty_0210 {

        @Override
        public Attribute createAttribute(HttpRequest request, String name, long definedSize) {
            if (useDisk) {
                Attribute attribute = new DiskAttribute(name, definedSize, charset, baseDir, deleteOnExit);
                return addToList(request, attribute);
            }
            if (checkSize) {
                Attribute attribute = new MixedAttribute(name, definedSize, minSize, charset, baseDir, deleteOnExit);
                return addToList(request, attribute);
            }
            MemoryAttribute attribute = new MemoryAttribute(name, definedSize);
            attribute.setMaxSize(maxSize);
            return attribute;
        }

        private Attribute addToList(HttpRequest request, Attribute attribute) {
            attribute.setMaxSize(maxSize);
            List<HttpData> list = getList(request);
            list.add(attribute);
            return attribute;
        }
}
