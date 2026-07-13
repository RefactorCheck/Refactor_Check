public class rxjava_0173 {

            @Override
            @SuppressWarnings("unchecked")
            public T[] getValues(T[] array) {
                Node<Object> headNode = head;
                int count = size();
    
                if (count == 0) {
                    if (array.length != 0) {
                        array[0] = null;
                    }
                } else {
                    if (array.length < count) {
                        array = (T[])Array.newInstance(array.getClass().getComponentType(), count);
                    }
    
                    int index = 0;
                    while (index != count) {
                        Node<Object> next = headNode.get();
                        array[index] = (T)next.value;
                        index++;
                        headNode = next;
                    }
                    if (array.length > count) {
                        array[count] = null;
                    }
                }
    
                return array;
            }
}
