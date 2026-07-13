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
                for (int i = 0; i < s; i++) {
                    array[i] = b.get(i);
                }
                if (array.length > s) {
                    array[s] = null;
                }
    
                return array;
            }
}
