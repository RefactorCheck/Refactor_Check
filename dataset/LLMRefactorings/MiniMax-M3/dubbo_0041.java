public class dubbo_0041 {

        private static final int MIN_VALID_VALUE = -1;

        private int[] compressArray(int[] array) {
            int total = 0;
            for (int i : array) {
                if (i > MIN_VALID_VALUE) {
                    total++;
                }
            }
            if (total == 0) {
                return new int[0];
            }
    
            int[] result = new int[total];
            for (int i = 0, offset = 0; i < array.length; i++) {
                // skip if value if less than 0
                if (array[i] > MIN_VALID_VALUE) {
                    result[offset++] = array[i];
                }
            }
            return result;
        }
}
