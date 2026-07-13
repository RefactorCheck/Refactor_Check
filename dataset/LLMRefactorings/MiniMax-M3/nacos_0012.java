import java.util.Collection;
import java.util.Iterator;

public class nacos_0012 {

    public static String collectionToDelimitedString(Collection<?> coll, String delim,
            String prefix, String suffix) {
        
        if (CollectionUtils.isEmpty(coll)) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder(calculateTotalLength(coll, delim, prefix, suffix));
        appendDelimitedElements(sb, coll.iterator(), delim, prefix, suffix);
        return sb.toString();
    }
    
    private static int calculateTotalLength(Collection<?> coll, String delim, String prefix, String suffix) {
        int totalLength =
            coll.size() * (prefix.length() + suffix.length()) + (coll.size() - 1) * delim.length();
        for (Object element : coll) {
            totalLength += String.valueOf(element).length();
        }
        return totalLength;
    }
    
    private static void appendDelimitedElements(StringBuilder sb, Iterator<?> it, String delim, String prefix, String suffix) {
        while (it.hasNext()) {
            sb.append(prefix).append(it.next()).append(suffix);
            if (it.hasNext()) {
                sb.append(delim);
            }
        }
    }
}
