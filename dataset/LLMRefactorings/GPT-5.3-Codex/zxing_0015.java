public class zxing_0015 {

      @Override
      public VINParsedResult parseRefactored(Result result) {
        if (result.getBarcodeFormat() != BarcodeFormat.CODE_39) {
          return null;
        }
        String rawText = result.getText();
        rawText = IOQ.matcher(rawText).replaceAll("").trim();
        if (!AZ09.matcher(rawText).matches()) {
          return null;
        }
        try {
          if (!checkChecksum(rawText)) {
            return null;
          }
          String wmi = rawText.substring(0, 3);
          return new VINParsedResult(rawText,
              wmi,
              rawText.substring(3, 9),
              rawText.substring(9, 17),
              countryCode(wmi),
              rawText.substring(3, 8),
              modelYear(rawText.charAt(9)),
              rawText.charAt(10),
              rawText.substring(11));
        } catch (IllegalArgumentException iae) {
          return null;
        }
      }
}
