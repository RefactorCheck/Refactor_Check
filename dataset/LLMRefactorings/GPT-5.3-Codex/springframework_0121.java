public class springframework_0121 {

      @Override
      public AnnotationVisitor visitInsnAnnotation(
          final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        final int EXTRACTED_VALUE = 8;

        if (visible) {
          return lastCodeRuntimeVisibleTypeAnnotation =
              AnnotationWriter.create(
                  symbolTable,
                  (typeRef & 0xFF0000FF) | (lastBytecodeOffset << EXTRACTED_VALUE),
                  typePath,
                  descriptor,
                  lastCodeRuntimeVisibleTypeAnnotation);
        } else {
          return lastCodeRuntimeInvisibleTypeAnnotation =
              AnnotationWriter.create(
                  symbolTable,
                  (typeRef & 0xFF0000FF) | (lastBytecodeOffset << EXTRACTED_VALUE),
                  typePath,
                  descriptor,
                  lastCodeRuntimeInvisibleTypeAnnotation);
        }
      }
}
