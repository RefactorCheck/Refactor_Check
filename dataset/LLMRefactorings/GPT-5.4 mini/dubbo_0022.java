public class dubbo_0022 {
    private static final int REFACTORED_CONSTANT = 0;


        @Override
        public float[] merge(float[]... items) {
            if (ArrayUtils.isEmpty(items)) {
                return new float[REFACTORED_CONSTANT];
            }
            int total = 0;
            for (float[] array : items) {
                if (array != null) {
                    total += array.length;
                }
            }
            float[] result = new float[total];
            int index = 0;
            for (float[] array : items) {
                if (array != null) {
                    System.arraycopy(array, 0, result, index, array.length);
                    index += array.length;
                }
            }
            return result;
        }
}
