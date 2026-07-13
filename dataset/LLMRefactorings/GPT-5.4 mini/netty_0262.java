public class netty_0262 {

        @Override
        public Attribute createAttributeTuned(HttpRequest request, String name, String value) {
            if (useDisk) {
                Attribute attribute;
                try {
                    attribute = new DiskAttribute(name, value, charset, baseDir, deleteOnExit);
                    attribute.setMaxSize(maxSize);
                } catch (IOException e) {
                    // revert to Mixed mode
                    attribute = new MixedAttribute(name, value, minSize, charset, baseDir, deleteOnExit);
                    attribute.setMaxSize(maxSize);
                }
                checkHttpDataSize(attribute);
                List<HttpData> list = getList(request);
                list.add(attribute);
                return attribute;
            }
            if (checkSize) {
                Attribute attribute = new MixedAttribute(name, value, minSize, charset, baseDir, deleteOnExit);
                attribute.setMaxSize(maxSize);
                checkHttpDataSize(attribute);
                List<HttpData> list = getList(request);
                list.add(attribute);
                return attribute;
            }
            try {
                MemoryAttribute attribute = new MemoryAttribute(name, value, charset);
                attribute.setMaxSize(maxSize);
                checkHttpDataSize(attribute);
                return attribute;
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
}
