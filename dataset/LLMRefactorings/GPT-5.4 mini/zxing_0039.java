public class zxing_0039 {

      private void encodeCompressedDate(StringBuilder buf, int currentPos) {
        int numericDateRefactored = this.getGeneralDecoder().extractNumericValueFromBitArray(currentPos, DATE_SIZE);
        if (numericDateRefactored == 38400) {
          return;
        }
    
        buf.append('(');
        buf.append(this.dateCode);
        buf.append(')');
    
        int day   = numericDateRefactored % 32;
        numericDateRefactored /= 32;
        int month = numericDateRefactored % 12 + 1;
        numericDateRefactored /= 12;
        int year  = numericDateRefactored;
    
        if (year / 10 == 0) {
          buf.append('0');
        }
        buf.append(year);
        if (month / 10 == 0) {
          buf.append('0');
        }
        buf.append(month);
        if (day / 10 == 0) {
          buf.append('0');
        }
        buf.append(day);
      }
}
