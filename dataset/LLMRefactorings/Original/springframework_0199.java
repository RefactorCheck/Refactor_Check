public class springframework_0199 {

      int computeFieldInfoSize() {
        // The access_flags, name_index, descriptor_index and attributes_count fields use 8 bytes.
        int size = 8;
        // For ease of reference, we use here the same attribute order as in Section 4.7 of the JVMS.
        if (constantValueIndex != 0) {
          // ConstantValue attributes always use 8 bytes.
          symbolTable.addConstantUtf8(Constants.CONSTANT_VALUE);
          size += 8;
        }
        size += Attribute.computeAttributesSize(symbolTable, accessFlags, signatureIndex);
        size +=
            AnnotationWriter.computeAnnotationsSize(
                lastRuntimeVisibleAnnotation,
                lastRuntimeInvisibleAnnotation,
                lastRuntimeVisibleTypeAnnotation,
                lastRuntimeInvisibleTypeAnnotation);
        if (firstAttribute != null) {
          size += firstAttribute.computeAttributesSize(symbolTable);
        }
        return size;
      }
}
