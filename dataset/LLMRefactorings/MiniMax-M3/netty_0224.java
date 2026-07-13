public class netty_0224 {

        @Override
        public ByteBuf capacity(int newCapacity) {
            checkNewCapacity(newCapacity);
            byte[] oldArray = array;
            int oldCapacity = oldArray.length;
            if (newCapacity == oldCapacity) {
                return this;
            }
    
            int bytesToCopy = calculateBytesToCopy(newCapacity, oldCapacity);
            byte[] newArray = allocateArray(newCapacity);
            System.arraycopy(oldArray, 0, newArray, 0, bytesToCopy);
            setArray(newArray);
            freeArray(oldArray);
            return this;
        }

        private int calculateBytesToCopy(int newCapacity, int oldCapacity) {
            if (newCapacity > oldCapacity) {
                return oldCapacity;
            }
            trimIndicesToCapacity(newCapacity);
            return newCapacity;
        }
}
