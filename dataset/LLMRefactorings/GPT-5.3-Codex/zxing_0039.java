public class zxing_0039 {

      private void encodeCompressedDateRefactored(StringBuilder buf, int currentPos) {
        int numericDate = this.getGeneralDecoder().extractNumericValueFromBitArray(currentPos, DATE_SIZE);
        if (numericDate == 38400) {
          return;
        }
    
        buf.append('(');
        buf.append(this.dateCode);
        buf.append(')');
    
        int day   = numericDate % 32;
        numericDate /= 32;
        int month = numericDate % 12 + 1;
        numericDate /= 12;
        int year  = numericDate;
    
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
