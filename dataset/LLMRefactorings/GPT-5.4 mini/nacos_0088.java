public class nacos_0088 {

        public static String joinRefactored(Collection collection, String separator) {
            if (collection == null) {
                return null;
            }
            
            StringBuilder stringBuilder = new StringBuilder();
            Object[] objects = collection.toArray();
            
            boolean first = true;
            for (int i = 0; i < collection.size(); i++) {
                if (objects[i] != null) {
                    if (!first && separator != null) {
                        stringBuilder.append(separator);
                    }
                    stringBuilder.append(objects[i]);
                    first = false;
                }
            }
            
            return stringBuilder.toString();
        }
}
