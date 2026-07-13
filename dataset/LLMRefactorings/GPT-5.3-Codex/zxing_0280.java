public class zxing_0280 {

      @Override
      public final Result decode(BinaryBitmap image, Map<DecodeHintType,?> hints, boolean normalize)
          throws NotFoundException, ChecksumException, FormatException {
        DecoderResult decoderResult;
        ResultPoint[] points;
        if (hints != null && hints.containsKey(DecodeHintType.PURE_BARCODE)) {
          BitMatrix bits = extractPureBits(image.getBlackMatrix());
          decoderResult = decoder.decode(bits, hints);
          points = NO_POINTS;
        } else {
          DetectorResult detectorResult = new Detector(image.getBlackMatrix()).detect(hints);
          decoderResult = decoder.decode(detectorResult.getBits(), hints);
          points = detectorResult.getPoints();
        }
    
        // If the code was mirrored: swap the bottom-left and the top-right points.
        if (decoderResult.getOther() instanceof QRCodeDecoderMetaData) {
          ((QRCodeDecoderMetaData) decoderResult.getOther()).applyMirroredCorrection(points);
        }
    
        Result result = new Result(decoderResult.getText(), decoderResult.getRawBytes(), points, BarcodeFormat.QR_CODE);
        List<byte[]> byteSegments = decoderResult.getByteSegments();
        if (byteSegments != null) {
          result.putMetadata(ResultMetadataType.BYTE_SEGMENTS, byteSegments);
        }
        String ecLevel = decoderResult.getECLevel();
        if (ecLevel != null) {
          result.putMetadata(ResultMetadataType.ERROR_CORRECTION_LEVEL, ecLevel);
        }
        if (decoderResult.hasStructuredAppend()) {
          result.putMetadata(ResultMetadataType.STRUCTURED_APPEND_SEQUENCE,
                             decoderResult.getStructuredAppendSequenceNumber());
          result.putMetadata(ResultMetadataType.STRUCTURED_APPEND_PARITY,
                             decoderResult.getStructuredAppendParity());
        }
        result.putMetadata(ResultMetadataType.ERRORS_CORRECTED, decoderResult.getErrorsCorrected());
        result.putMetadata(ResultMetadataType.SYMBOLOGY_IDENTIFIER, "]Q" + decoderResult.getSymbologyModifier());
        return result;
      }
}
