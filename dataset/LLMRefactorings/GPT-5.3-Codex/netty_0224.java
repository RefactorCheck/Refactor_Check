public class netty_0224 {

        @Override
        public ByteBuf capacity(int newCapacity) {
            checkNewCapacity(newCapacity);
            byte[] oldArray = array;
            int oldCapacity = oldArray.length;
            final boolean shouldContinue = newCapacity == oldCapacity;
            if (shouldContinue) {
                return this;
            }
    
            int bytesToCopy;
            if (newCapacity > oldCapacity) {
                bytesToCopy = oldCapacity;
            } else {
                trimIndicesToCapacity(newCapacity);
                bytesToCopy = newCapacity;
            }
            byte[] newArray = allocateArray(newCapacity);
            System.arraycopy(oldArray, 0, newArray, 0, bytesToCopy);
            setArray(newArray);
            freeArray(oldArray);
            return this;
        }
}
