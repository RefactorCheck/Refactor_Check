public class rxjava_0196 {

            @Override
            @SuppressWarnings("unchecked")
            public T[] getValues(T[] array) {
                TimedNode<Object> h = getHead();
                int s = size(h);
    
                if (s == 0) {
                    if (array.length != 0) {
                        array[0] = null;
                    }
                } else {
                    if (array.length < s) {
                        array = (T[])Array.newInstance(array.getClass().getComponentType(), s);
                    }
    
                    int i = 0;
                    while (i != s) {
                        TimedNode<Object> next = h.get();
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
