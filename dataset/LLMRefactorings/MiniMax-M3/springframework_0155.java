public class springframework_0155 {

      private void readParameterAnnotations(
          final MethodVisitor methodVisitor,
          final Context context,
          final int runtimeParameterAnnotationsOffset,
          final boolean visible) {
        int currentOffset = runtimeParameterAnnotationsOffset;
        int numParameters = classFileBuffer[currentOffset++] & 0xFF;
        methodVisitor.visitAnnotableParameterCount(numParameters, visible);
        char[] charBuffer = context.charBuffer;
        for (int i = 0; i < numParameters; ++i) {
          currentOffset = readAnnotationsForParameter(methodVisitor, currentOffset, charBuffer, i, visible);
        }
      }

      private int readAnnotationsForParameter(
          final MethodVisitor methodVisitor,
          int currentOffset,
          final char[] charBuffer,
          final int parameterIndex,
          final boolean visible) {
        int numAnnotations = readUnsignedShort(currentOffset);
        currentOffset += 2;
        while (numAnnotations-- > 0) {
          // Parse the type_index field.
          String annotationDescriptor = readUTF8(currentOffset, charBuffer);
          currentOffset += 2;
          // Parse num_element_value_pairs and element_value_pairs and visit these values.
          currentOffset =
              readElementValues(
                  methodVisitor.visitParameterAnnotation(parameterIndex, annotationDescriptor, visible),
                  currentOffset,
                  /* named= */ true,
                  charBuffer);
        }
        return currentOffset;
      }
}
