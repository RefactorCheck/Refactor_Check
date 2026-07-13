public class springframework_0123 {

      void putRecordComponentInfo(final ByteVector output) {
        output.putShort(nameIndex).putShort(descriptorIndex);
        output.putShort(computeAttributesCount());
        Attribute.putAttributes(symbolTable, 0, signatureIndex, output);
        AnnotationWriter.putAnnotations(
            symbolTable,
            lastRuntimeVisibleAnnotation,
            lastRuntimeInvisibleAnnotation,
            lastRuntimeVisibleTypeAnnotation,
            lastRuntimeInvisibleTypeAnnotation,
            output);
        if (firstAttribute != null) {
          firstAttribute.putAttributes(symbolTable, output);
        }
      }

      private int computeAttributesCount() {
        int attributesCount = 0;
        if (signatureIndex != 0) {
          ++attributesCount;
        }
        if (lastRuntimeVisibleAnnotation != null) {
          ++attributesCount;
        }
        if (lastRuntimeInvisibleAnnotation != null) {
          ++attributesCount;
        }
        if (lastRuntimeVisibleTypeAnnotation != null) {
          ++attributesCount;
        }
        if (lastRuntimeInvisibleTypeAnnotation != null) {
          ++attributesCount;
        }
        if (firstAttribute != null) {
          attributesCount += firstAttribute.getAttributeCount();
        }
        return attributesCount;
      }
}
