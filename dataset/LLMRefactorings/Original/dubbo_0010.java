public class dubbo_0010 {

            @Override
            public synchronized E previous() {
                if (isInTailList) {
                    boolean hasPreviousInTailList = tailListIterator.hasPrevious();
                    if (hasPreviousInTailList) {
                        lastReturnedIndex = index;
                        index -= 1;
                        return tailListIterator.previous();
                    } else {
                        int lastIndexInBit = bitList.rootSet.previousSetBit(bitList.rootSet.size());
                        if (lastIndexInBit == -1) {
                            throw new NoSuchElementException();
                        } else {
                            isInTailList = false;
                            curBitIndex = bitList.rootSet.previousSetBit(lastIndexInBit - 1);
                            lastReturnedIndex = index;
                            index -= 1;
                            return bitList.originList.get(lastIndexInBit);
                        }
                    }
                } else {
                    if (curBitIndex == -1) {
                        throw new NoSuchElementException();
                    }
                    int nextBitIndex = curBitIndex;
                    curBitIndex = bitList.rootSet.previousSetBit(curBitIndex - 1);
                    lastReturnedIndex = index;
                    index -= 1;
                    return bitList.originList.get(nextBitIndex);
                }
            }
}
