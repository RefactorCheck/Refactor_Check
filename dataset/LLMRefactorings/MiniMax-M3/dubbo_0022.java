public class dubbo_0022 {

        @Override
        public float[] merge(float[]... items) {
            if (ArrayUtils.isEmpty(items)) {
                return new float[0];
            }
            int total = calculateTotalLength(items);
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

        private int calculateTotalLength(float[][] items) {
            int total = 0;
            for (float[] array : items) {
                if (array != null) {
                    total += array.length;
                }
            }
            return total;
        }
}
