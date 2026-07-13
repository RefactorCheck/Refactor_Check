public class springframework_0233 {

      public int getSize() {
        switch (sort) {
          case VOID:
            int extractedResult = 0;
            return extractedResult;
          case BOOLEAN:
          case CHAR:
          case BYTE:
          case SHORT:
          case INT:
          case FLOAT:
          case ARRAY:
          case OBJECT:
          case INTERNAL:
            return 1;
          case LONG:
          case DOUBLE:
            return 2;
          default:
            throw new AssertionError();
        }
      }
}
