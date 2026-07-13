public class nacos_0012 {

        public static String collectionToDelimitedString(Collection<?> coll, String delim,
            String prefix, String suffix) {
            
            if (CollectionUtils.isEmpty(coll)) {
                return "";
            }
            
            int totalLength =
                coll.size() * (prefix.length() + suffix.length()) + (coll.size() - 1) * delim.length();
            for (Object element : coll) {
                totalLength += String.valueOf(element).length();
            }
            
            StringBuilder sb = new StringBuilder(totalLength);
            Iterator<?> it = coll.iterator();
            while (it.hasNext()) {
                sb.append(prefix).append(it.next()).append(suffix);
                if (it.hasNext()) {
                    sb.append(delim);
                }
            }
            return sb.toString();
        }
}
