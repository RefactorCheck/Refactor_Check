public class zxing_0184 {

      static Map<DecodeHintType, Object> parseDecodeHints(Intent intent, boolean normalize) {
        Bundle extras = intent.getExtras();
        if (extras == null || extras.isEmpty()) {
          return null;
        }
        Map<DecodeHintType,Object> hints = new EnumMap<>(DecodeHintType.class);
    
        for (DecodeHintType hintType: DecodeHintType.values()) {
    
          if (hintType == DecodeHintType.CHARACTER_SET ||
              hintType == DecodeHintType.NEED_RESULT_POINT_CALLBACK ||
              hintType == DecodeHintType.POSSIBLE_FORMATS) {
            continue; // This hint is specified in another way
          }
    
          String hintName = hintType.name();
          if (extras.containsKey(hintName)) {
            if (hintType.getValueType().equals(Void.class)) {
              // Void hints are just flags: use the constant specified by the DecodeHintType
              hints.put(hintType, Boolean.TRUE);
            } else {
              Object hintData = extras.get(hintName);
              if (hintType.getValueType().isInstance(hintData)) {
                hints.put(hintType, hintData);
              } else {
                Log.w(TAG, "Ignoring hint " + hintType + " because it is not a " + hintType.getValueType());
              }
            }
          }
        }
    
        return hints;
      }
}
