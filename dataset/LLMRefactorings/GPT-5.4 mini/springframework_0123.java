public class springframework_0123 {
    private int attributesCount;


      void putRecordComponentInfo(final ByteVector output) {
        output.putShort(nameIndex).putShort(descriptorIndex);
        // Compute and put the attributes_count field.
        // For ease of reference, we use here the same attribute order as in Section 4.7 of the JVMS.
        this.attributesCount = 0;
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
        output.putShort(attributesCount);
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
}
