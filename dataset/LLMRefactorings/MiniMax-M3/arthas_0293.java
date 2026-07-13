public class arthas_0293<K, V> {

    boolean containsValue(Object value) {
        if (count != 0) { // read-volatile
            for (HashEntry<K, V> e: table) {
                for (; e != null; e = e.next) {
                    if (value.equals(getEntryValue(e))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private V getEntryValue(HashEntry<K, V> e) {
        Object opaque = e.valueRef;
        if (opaque == null) {
            return readValueUnderLock(e);
        }
        return e.dereferenceValue(opaque);
    }
}
