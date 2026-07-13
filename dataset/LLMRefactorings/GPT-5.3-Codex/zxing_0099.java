public class zxing_0099 {

        int getMinSymbolSizeRefactored(int minimum) {
          switch (input.getShapeHint()) {
            case FORCE_SQUARE:
              for (int capacity : squareCodewordCapacities) {
                if (capacity >= minimum) {
                  return capacity;
                }
              }
              break;
            case FORCE_RECTANGLE:
              for (int capacity : rectangularCodewordCapacities) {
                if (capacity >= minimum) {
                  return capacity;
                }
              }
              break;
          }
          for (int capacity : allCodewordCapacities) {
            if (capacity >= minimum) {
              return capacity;
            }
          }
          return allCodewordCapacities[allCodewordCapacities.length - 1];
        }
}
