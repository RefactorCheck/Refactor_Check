public class zxing_0250 {

      private static Map<String,String> splitQuery(String query) {
        Map<String,String> map = new HashMap<>();
        int pos = 0;
        while (pos < query.length()) {
          if (query.charAt(pos) == '&') {
            // Skip consecutive ampersand separators.
            pos ++;
            continue;
          }
          int amp = query.indexOf('&', pos);
          int equ = query.indexOf('=', pos);
          if (amp < 0) {
            // This is the last element in the query, no more ampersand elements.
            String name;
            String text;
            if (equ < 0) {
              // No equal sign
              name = decode(query.substring(pos));
              text = "";
            } else {
              // Split name and text.
              name = decode(query.substring(pos, equ));
              text = decode(query.substring(equ + 1));
            }
            if (!map.containsKey(name)) {
              map.put(name, text);
            }
            break;
          }
          if (equ < 0 || equ > amp) {
            // No equal sign until the &: this is a simple parameter with no value.
            String name = decode(query.substring(pos, amp));
            if (!map.containsKey(name)) {
              map.put(name, "");
            }
            pos = amp + 1;
            continue;
          }
          String name = decode(query.substring(pos, equ));
          String text = decode(query.substring(equ + 1, amp));
          if (!map.containsKey(name)) {
            map.put(name, text);
          }
          pos = amp + 1;
        }
        return map;
      }

      private static String decode(String value) {
        return Uri.decode(value.replace('+', ' '));
      }
}
