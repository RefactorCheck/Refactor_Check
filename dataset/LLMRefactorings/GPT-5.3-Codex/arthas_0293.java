public class arthas_0293 {

            boolean containsValueRefactored(Object value) {
                if (count != 0) { // read-volatile
                    for (HashEntry<K, V> e: table) {
                        for (; e != null; e = e.next) {
                            Object opaque = e.valueRef;
                            V v;
    
                            if (opaque == null) {
                                v = readValueUnderLock(e); // recheck
                            } else {
                                v = e.dereferenceValue(opaque);
                            }
    
                            if (value.equals(v)) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
}
