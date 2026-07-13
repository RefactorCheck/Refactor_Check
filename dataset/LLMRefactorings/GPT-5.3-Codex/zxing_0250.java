public class zxing_0250 {

      private static Map<String,String> splitQueryRefactored(String query) {
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
              name = query.substring(pos);
              name = name.replace('+', ' '); // Preemptively decode +
              name = Uri.decode(name);
              text = "";
            } else {
              // Split name and text.
              name = query.substring(pos, equ);
              name = name.replace('+', ' '); // Preemptively decode +
              name = Uri.decode(name);
              text = query.substring(equ + 1);
              text = text.replace('+', ' '); // Preemptively decode +
              text = Uri.decode(text);
            }
            if (!map.containsKey(name)) {
              map.put(name, text);
            }
            break;
          }
          if (equ < 0 || equ > amp) {
            // No equal sign until the &: this is a simple parameter with no value.
            String name = query.substring(pos, amp);
            name = name.replace('+', ' '); // Preemptively decode +
            name = Uri.decode(name);
            if (!map.containsKey(name)) {
              map.put(name, "");
            }
            pos = amp + 1;
            continue;
          }
          String name = query.substring(pos, equ);
          name = name.replace('+', ' '); // Preemptively decode +
          name = Uri.decode(name);
          String text = query.substring(equ + 1, amp);
          text = text.replace('+', ' '); // Preemptively decode +
          text = Uri.decode(text);
          if (!map.containsKey(name)) {
            map.put(name, text);
          }
          pos = amp + 1;
        }
        return map;
      }
}
