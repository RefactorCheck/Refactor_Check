public class dubbo_0064 {

        @Override
        public short[] merge(short[]... items) {
            if (ArrayUtils.isEmpty(items)) {
                return new short[0];
            }
            int total = calculateTotalLength(items);
            short[] result = new short[total];
            int index = 0;
            for (short[] array : items) {
                if (array != null) {
                    System.arraycopy(array, 0, result, index, array.length);
                    index += array.length;
                }
            }
            return result;
        }

        private int calculateTotalLength(short[]... items) {
            int total = 0;
            for (short[] array : items) {
                if (array != null) {
                    total += array.length;
                }
            }
            return total;
        }
}
