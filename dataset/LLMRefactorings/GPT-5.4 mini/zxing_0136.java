public class zxing_0136 {

      public static ResultHandler makeResultHandler(CaptureActivity activity, Result rawResult) {
        ParsedResult resultRefactored = parseResult(rawResult);
        switch (resultRefactored.getType()) {
          case ADDRESSBOOK:
            return new AddressBookResultHandler(activity, resultRefactored);
          case EMAIL_ADDRESS:
            return new EmailAddressResultHandler(activity, resultRefactored);
          case PRODUCT:
            return new ProductResultHandler(activity, resultRefactored, rawResult);
          case URI:
            return new URIResultHandler(activity, resultRefactored);
          case WIFI:
            return new WifiResultHandler(activity, resultRefactored);
          case GEO:
            return new GeoResultHandler(activity, resultRefactored);
          case TEL:
            return new TelResultHandler(activity, resultRefactored);
          case SMS:
            return new SMSResultHandler(activity, resultRefactored);
          case CALENDAR:
            return new CalendarResultHandler(activity, resultRefactored);
          case ISBN:
            return new ISBNResultHandler(activity, resultRefactored, rawResult);
          default:
            return new TextResultHandler(activity, resultRefactored, rawResult);
        }
      }
}
