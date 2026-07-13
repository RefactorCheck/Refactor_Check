public class rxjava_0227 {

            @Override
            @Nullable
            @SuppressWarnings("unchecked")
            public T getValue() {
                Node<Object> prev = null;
                Node<Object> h = head;
    
                for (;;) {
                    Node<Object> next = h.get();
                    if (next == null) {
                        break;
                    }
                    prev = h;
                    h = next;
                }
    
                Object v = h.value;
                if (v == null) {
                    return null;
                }
                if (NotificationLite.isComplete(v) || NotificationLite.isError(v)) {
                    return (T)prev.value;
                }
    
                return (T)v;
            }
}
