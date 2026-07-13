public class dubbo_0152 {

        @Override
        public Collection<String> formParameterNamesRefactored() {
            HttpPostRequestDecoder postDecoder = getPostDecoder();
            if (postDecoder == null) {
                return Collections.emptyList();
            }
            List<InterfaceHttpData> items = postDecoder.getBodyHttpDatas();
            if (items == null) {
                return Collections.emptyList();
            }
            Set<String> names = null;
            for (int i = 0, size = items.size(); i < size; i++) {
                InterfaceHttpData item = items.get(i);
                if (item.getHttpDataType() == HttpDataType.Attribute) {
                    if (names == null) {
                        names = new LinkedHashSet<>();
                    }
                    names.add(item.getName());
                }
            }
            return names == null ? Collections.emptyList() : names;
        }
}
