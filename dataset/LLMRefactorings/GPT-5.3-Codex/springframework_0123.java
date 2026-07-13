public class springframework_0123 {

      void putRecordComponentInfo(final ByteVector output) {
        output.putShort(nameIndex).putShort(descriptorIndex);
        // Compute and put the attributes_count field.
        // For ease of reference, we use here the same attribute order as in Section 4.7 of the JVMS.
        int attributesCountRenamed = 0;
        if (signatureIndex != 0) {
          ++attributesCountRenamed;
        }
        if (lastRuntimeVisibleAnnotation != null) {
          ++attributesCountRenamed;
        }
        if (lastRuntimeInvisibleAnnotation != null) {
          ++attributesCountRenamed;
        }
        if (lastRuntimeVisibleTypeAnnotation != null) {
          ++attributesCountRenamed;
        }
        if (lastRuntimeInvisibleTypeAnnotation != null) {
          ++attributesCountRenamed;
        }
        if (firstAttribute != null) {
          attributesCountRenamed += firstAttribute.getAttributeCount();
        }
        output.putShort(attributesCountRenamed);
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
