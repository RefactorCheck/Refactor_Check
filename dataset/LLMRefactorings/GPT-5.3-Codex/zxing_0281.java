public class zxing_0281 {
static @Override public Result decode(BinaryBitmap image, Map<DecodeHintType,?> hints)
          throws NotFoundException, ChecksumException, FormatException {
        DecoderResult decoderResult;
        ResultPoint[] points;
        if (hints != null && hints.containsKey(DecodeHintType.PURE_BARCODE)) {
          BitMatrix bits = extractPureBits(image.getBlackMatrix());
          decoderResult = decoder.decode(bits);
          points = NO_POINTS;
        } else {
          DetectorResult detectorResult = new Detector(image.getBlackMatrix()).detect();
          decoderResult = decoder.decode(detectorResult.getBits());
          points = detectorResult.getPoints();
        }
        Result result = new Result(decoderResult.getText(), decoderResult.getRawBytes(), points,
            BarcodeFormat.DATA_MATRIX);
        List<byte[]> byteSegments = decoderResult.getByteSegments();
        if (byteSegments != null) {
          result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, byteSegments);
        }
        String ecLevel = decoderResult.getECLevel();
        if (ecLevel != null) {
          result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, ecLevel);
        }
        result.putMetadata(ResultMetadataType.READER_INIT, decoderResult.getReaderInit());
        result.putMetadata(ResultMetadataType.ERRORS_CORRECTED, decoderResult.getErrorsCorrected());
        result.putMetadata(ResultMetadataType.SYMBOLOGY_IDENTIFIER, "]d" + decoderResult.getSymbologyModifier());
        return result;
      }
}
