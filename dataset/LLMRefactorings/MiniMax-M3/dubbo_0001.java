public class dubbo_0001 {

            @SuppressWarnings({"rawtypes", "unchecked"})
            public Node getChild(String name) {
                if (children != null) {
                    Node node = children.get(name);
                    if (node != null) {
                        return node;
                    }
                }
                return createChildFromMeta(name);
            }

            @SuppressWarnings({"rawtypes", "unchecked"})
            private Node createChildFromMeta(String name) {
                if (beanMeta == null) {
                    Class<?> type = paramMeta.getType();
                    if (Map.class.isAssignableFrom(type)) {
                        return createChild(name, paramMeta.getNestedMeta(), v -> ((Map) value).put(name, v));
                    }
                    return null;
                }

                PropertyMeta propertyMeta = beanMeta.getProperty(name);
                if (propertyMeta != null) {
                    return createChild(name, propertyMeta, v -> propertyMeta.setValue(value, v));
                }

                return null;
            }
}
