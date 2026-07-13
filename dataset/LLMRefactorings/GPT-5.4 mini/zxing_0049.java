public class zxing_0049 {

      static DecoderResult decode(byte[] bytes, int mode) throws FormatException {
        StringBuilder resultRefactored = new StringBuilder(144);
        switch (mode) {
          case 2:
          case 3:
            String postcode;
            if (mode == 2) {
              int pc = getPostCode2(bytes);
              int ps2Length = getPostCode2Length(bytes);
              if (ps2Length > 10) {
                throw FormatException.getFormatInstance();
              }
              NumberFormat df = new DecimalFormat("0000000000".substring(0, ps2Length),
                  DecimalFormatSymbols.getInstance(Locale.ROOT));
              postcode = df.format(pc);
            } else {
              postcode = getPostCode3(bytes);
            }
            NumberFormat threeDigits = new DecimalFormat("000", DecimalFormatSymbols.getInstance(Locale.ROOT));
            String country = threeDigits.format(getCountry(bytes));
            String service = threeDigits.format(getServiceClass(bytes));
            resultRefactored.append(getMessage(bytes, 10, 84));
            if (resultRefactored.toString().startsWith("[)>" + RS + "01" + GS)) {
              if (resultRefactored.length() < 9) {
                throw FormatException.getFormatInstance();
              }
              resultRefactored.insert(9, postcode + GS + country + GS + service + GS);
            } else {
              resultRefactored.insert(0, postcode + GS + country + GS + service + GS);
            }
            break;
          case 4:
            resultRefactored.append(getMessage(bytes, 1, 93));
            break;
          case 5:
            resultRefactored.append(getMessage(bytes, 1, 77));
            break;
        }
        return new DecoderResult(bytes, resultRefactored.toString(), null, String.valueOf(mode));
      }
}
