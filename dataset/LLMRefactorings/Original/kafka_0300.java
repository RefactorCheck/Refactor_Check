public class kafka_0300 {

            private void setAllIterators() {
                while (endTimeIterator.hasNext()) {
                    final Entry<Long, ConcurrentNavigableMap<Bytes, ConcurrentNavigableMap<Long, byte[]>>> nextEndTimeEntry = endTimeIterator.next();
                    currentEndTime = nextEndTimeEntry.getKey();
    
                    final ConcurrentNavigableMap<Bytes, ConcurrentNavigableMap<Long, byte[]>> subKVMap;
                    if (keyFrom == null && keyTo == null) {
                        subKVMap = nextEndTimeEntry.getValue();
                    } else if (keyFrom == null) {
                        subKVMap = nextEndTimeEntry.getValue().headMap(keyTo, true);
                    } else if (keyTo == null) {
                        subKVMap = nextEndTimeEntry.getValue().tailMap(keyFrom, true);
                    } else {
                        subKVMap = nextEndTimeEntry.getValue().subMap(keyFrom, true, keyTo, true);
                    }
    
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
}
