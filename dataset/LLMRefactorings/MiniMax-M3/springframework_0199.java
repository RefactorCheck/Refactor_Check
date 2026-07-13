public class springframework_0199 {

      int computeFieldInfoSize() {
        int size = 8;
        if (constantValueIndex != 0) {
          symbolTable.addConstantUtf8(Constants.CONSTANT_VALUE);
          size += 8;
        }
        size += Attribute.computeAttributesSize(symbolTable, accessFlags, signatureIndex);
        size += computeAnnotationsSize();
        if (firstAttribute != null) {
          size += firstAttribute.computeAttributesSize(symbolTable);
        }
        return size;
      }

      int computeAnnotationsSize() {
        return AnnotationWriter.computeAnnotationsSize(
            lastRuntimeVisibleAnnotation,
            lastRuntimeInvisibleAnnotation,
            lastRuntimeVisibleTypeAnnotation,
            lastRuntimeInvisibleTypeAnnotation);
      }
}
