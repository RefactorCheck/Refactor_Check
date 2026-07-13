public class springframework_0233 {

      public int getSizeInBytes() {
        switch (sort) {
          case VOID:
            return 0;
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
