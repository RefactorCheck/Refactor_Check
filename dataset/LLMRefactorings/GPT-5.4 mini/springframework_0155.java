public class springframework_0155 {
    private int currentOffset;


      private void readParameterAnnotations(
          final MethodVisitor methodVisitor,
          final Context context,
          final int runtimeParameterAnnotationsOffset,
          final boolean visible) {
        this.currentOffset = runtimeParameterAnnotationsOffset;
        int numParameters = classFileBuffer[currentOffset++] & 0xFF;
        methodVisitor.visitAnnotableParameterCount(numParameters, visible);
        char[] charBuffer = context.charBuffer;
        for (int i = 0; i < numParameters; ++i) {
          int numAnnotations = readUnsignedShort(currentOffset);
          currentOffset += 2;
          while (numAnnotations-- > 0) {
            // Parse the type_index field.
            String annotationDescriptor = readUTF8(currentOffset, charBuffer);
            currentOffset += 2;
            // Parse num_element_value_pairs and element_value_pairs and visit these values.
            currentOffset =
                readElementValues(
                    methodVisitor.visitParameterAnnotation(i, annotationDescriptor, visible),
                    currentOffset,
                    /* named= */ true,
                    charBuffer);
          }
        }
      }
}
