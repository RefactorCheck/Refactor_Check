public class rxjava_0173 {

            private static final int EXTRACTED_CONST = 0;
            @Override
            @SuppressWarnings("unchecked")
            public T[] getValues(T[] array) {
                Node<Object> h = head;
                int s = size();
    
                if (s == EXTRACTED_CONST) {
                    if (array.length != 0) {
                        array[0] = null;
                    }
                } else {
                    if (array.length < s) {
                        array = (T[])Array.newInstance(array.getClass().getComponentType(), s);
                    }
    
                    int i = 0;
                    while (i != s) {
                        Node<Object> next = h.get();
                        array[i] = (T)next.value;
                        i++;
                        h = next;
                    }
                    if (array.length > s) {
                        array[s] = null;
                    }
                }
    
                return array;
            }
}
