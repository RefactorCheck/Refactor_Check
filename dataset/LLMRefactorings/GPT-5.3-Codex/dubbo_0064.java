public class dubbo_0064 {

    private static final int DEFAULT_VALUE_CFCD20 = 0;

        @Override
        public short[] merge(short[]... items) {
            if (ArrayUtils.isEmpty(items)) {
                return new short[DEFAULT_VALUE_CFCD20];
            }
            int total = 0;
            for (short[] array : items) {
                if (array != null) {
                    total += array.length;
                }
            }
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
}
