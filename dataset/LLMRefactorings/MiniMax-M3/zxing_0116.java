public class zxing_0116 {

      private static Result[] decode(BinaryBitmap image, Map<DecodeHintType, ?> hints, boolean multiple)
          throws NotFoundException, FormatException, ChecksumException {
        List<Result> results = new ArrayList<>();
        PDF417DetectorResult detectorResult = Detector.detect(image, hints, multiple);
        for (ResultPoint[] points : detectorResult.getPoints()) {
          DecoderResult decoderResult = PDF417ScanningDecoder.decode(detectorResult.getBits(), points[4], points[5],
              points[6], points[7], getMinCodewordWidth(points), getMaxCodewordWidth(points));
          Result result = buildResult(decoderResult, points, detectorResult.getRotation());
          results.add(result);
        }
        return results.toArray(EMPTY_RESULT_ARRAY);
      }

      private static Result buildResult(DecoderResult decoderResult, ResultPoint[] points, int rotation) {
        Result result = new Result(decoderResult.getText(), decoderResult.getRawBytes(), points, BarcodeFormat.PDF_417);
        result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, decoderResult.getECLevel());
        result.putMetadata(ResultMetadataType.ERRORS_CORRECTED, decoderResult.getErrorsCorrected());
        result.putMetadata(ResultMetadataType.ERASURES_CORRECTED, decoderResult.getErasures());
        PDF417ResultMetadata pdf417ResultMetadata = (PDF417ResultMetadata) decoderResult.getOther();
        if (pdf417ResultMetadata != null) {
          result.putMetadata(ResultMetadataType.PDF417_EXTRA_METADATA, pdf417ResultMetadata);
        }
        result.putMetadata(ResultMetadataType.ORIENTATION, rotation);
        result.putMetadata(ResultMetadataType.READER_INIT, decoderResult.getReaderInit());
        result.putMetadata(ResultMetadataType.SYMBOLOGY_IDENTIFIER, "]L" + decoderResult.getSymbologyModifier());
        return result;
      }
}
