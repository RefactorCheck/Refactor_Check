public class zxing_0076 {

      private void encodeFromStreamExtra(Intent intent) throws WriterException {
        format = BarcodeFormat.QR_CODE;
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
          throw new WriterException("No extras");
        }
        Uri uri = bundle.getParcelable(Intent.EXTRA_STREAM);
        if (uri == null) {
          throw new WriterException("No EXTRA_STREAM");
        }
        byte[] vcard;
        String vcardString;
        try (InputStream stream = activity.getContentResolver().openInputStream(uri)) {
          if (stream == null) {
            throw new WriterException("Can't open stream for " + uri);
          }
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          byte[] buffer = new byte[2048];
          int bytesRead;
          while ((bytesRead = stream.read(buffer)) > 0) {
            baos.write(buffer, 0, bytesRead);
          }
          vcard = baos.toByteArray();
          vcardString = new String(vcard, 0, vcard.length, StandardCharsets.UTF_8);
        } catch (IOException ioe) {
          throw new WriterException(ioe);
        }
        Result result = new Result(vcardString, vcard, null, BarcodeFormat.QR_CODE);
        ParsedResult parsedResult = ResultParser.parseResult(result);
        if (!(parsedResult instanceof AddressBookParsedResult)) {
          throw new WriterException("Result was not an address");
        }
        encodeQRCodeContents((AddressBookParsedResult) parsedResult);
        if (contents == null || contents.isEmpty()) {
          throw new WriterException("No content to encode");
        }
      }
}
