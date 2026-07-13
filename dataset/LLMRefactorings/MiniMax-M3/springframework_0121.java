public class springframework_0121 {

      @Override
      public AnnotationVisitor visitInsnAnnotation(
          final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
        final int annotationTypeRef = (typeRef & 0xFF0000FF) | (lastBytecodeOffset << 8);
        if (visible) {
          return lastCodeRuntimeVisibleTypeAnnotation =
              AnnotationWriter.create(
                  symbolTable,
                  annotationTypeRef,
                  typePath,
                  descriptor,
                  lastCodeRuntimeVisibleTypeAnnotation);
        } else {
          return lastCodeRuntimeInvisibleTypeAnnotation =
              AnnotationWriter.create(
                  symbolTable,
                  annotationTypeRef,
                  typePath,
                  descriptor,
                  lastCodeRuntimeInvisibleTypeAnnotation);
        }
      }
}
