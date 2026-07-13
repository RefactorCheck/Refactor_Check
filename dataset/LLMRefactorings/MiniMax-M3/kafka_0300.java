public class kafka_0300 {

            private void setAllIterators() {
                while (endTimeIterator.hasNext()) {
                    final Entry<Long, ConcurrentNavigableMap<Bytes, ConcurrentNavigableMap<Long, byte[]>>> nextEndTimeEntry = endTimeIterator.next();
                    currentEndTime = nextEndTimeEntry.getKey();

                    final ConcurrentNavigableMap<Bytes, ConcurrentNavigableMap<Long, byte[]>> subKVMap = selectSubKVMap(nextEndTimeEntry.getValue());

                    if (forward) {
                        keyIterator = subKVMap.entrySet().iterator();
                    } else {
                        keyIterator = subKVMap.descendingMap().entrySet().iterator();
                    }

                    if (setInnerIterators()) {
                        return;
                    }
                }
                recordIterator = null;
            }

            private ConcurrentNavigableMap<Bytes, ConcurrentNavigableMap<Long, byte[]>> selectSubKVMap(ConcurrentNavigableMap<Bytes, ConcurrentNavigableMap<Long, byte[]>> value) {
                if (keyFrom == null && keyTo == null) {
                    return value;
                } else if (keyFrom == null) {
                    return value.headMap(keyTo, true);
                } else if (keyTo == null) {
                    return value.tailMap(keyFrom, true);
                } else {
                    return value.subMap(keyFrom, true, keyTo, true);
                }
            }
}
