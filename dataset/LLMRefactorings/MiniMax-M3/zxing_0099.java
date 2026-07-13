public class zxing_0099 {

        int getMinSymbolSize(int minimum) {
          switch (input.getShapeHint()) {
            case FORCE_SQUARE:
              return findMinCapacity(squareCodewordCapacities, minimum);
            case FORCE_RECTANGLE:
              return findMinCapacity(rectangularCodewordCapacities, minimum);
            default:
              return findMinCapacity(allCodewordCapacities, minimum);
          }
        }
        
        private int findMinCapacity(int[] capacities, int minimum) {
            for (int capacity : capacities) {
                if (capacity >= minimum) {
                    return capacity;
                }
            }
            return capacities[capacities.length - 1];
        }
}
