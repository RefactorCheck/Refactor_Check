public class rxjava_0260 {

            @Override
            @SuppressWarnings("unchecked")
            public T[] getValues(T[] array) {
                int s = size;
                if (s == 0) {
                    if (array.length != 0) {
                        array[0] = null;
                    }
                    return array;
                }
                List<T> b = buffer;
    
                if (array.length < s) {
                    array = (T[])Array.newInstance(array.getClass().getComponentType(), s);
                }
                copyToArray(b, s, array);
                if (array.length > s) {
                    array[s] = null;
                }
    
                return array;
            }
    
            private void copyToArray(List<T> source, int count, T[] target) {
                for (int i = 0; i < count; i++) {
                    target[i] = source.get(i);
                }
            }
}
