public class netty_0210 {

        @Override
        public Attribute createAttributeAdjusted(HttpRequest request, String name, long definedSize) {
            if (useDisk) {
                Attribute attribute = new DiskAttribute(name, definedSize, charset, baseDir, deleteOnExit);
                attribute.setMaxSize(maxSize);
                List<HttpData> list = getList(request);
                list.add(attribute);
                return attribute;
            }
            if (checkSize) {
                Attribute attribute = new MixedAttribute(name, definedSize, minSize, charset, baseDir, deleteOnExit);
                attribute.setMaxSize(maxSize);
                List<HttpData> list = getList(request);
                list.add(attribute);
                return attribute;
            }
            MemoryAttribute attribute = new MemoryAttribute(name, definedSize);
            attribute.setMaxSize(maxSize);
            return attribute;
        }
}
