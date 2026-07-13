public class zxing_0139 {

      private void attachMoreExtras(Intent intent) {
        for (Map.Entry<String,Object> entry : moreExtras.entrySet()) {
          String key = entry.getKey();
          Object value = entry.getValue();
          putExtra(intent, key, value);
        }
      }

      private void putExtra(Intent intent, String key, Object value) {
        // Kind of hacky
        if (value instanceof Integer) {
          intent.putExtra(key, (Integer) value);
        } else if (value instanceof Long) {
          intent.putExtra(key, (Long) value);
        } else if (value instanceof Boolean) {
          intent.putExtra(key, (Boolean) value);
        } else if (value instanceof Double) {
          intent.putExtra(key, (Double) value);
        } else if (value instanceof Float) {
          intent.putExtra(key, (Float) value);
        } else if (value instanceof Bundle) {
          intent.putExtra(key, (Bundle) value);
        } else {
          intent.putExtra(key, value.toString());
        }
      }
}
