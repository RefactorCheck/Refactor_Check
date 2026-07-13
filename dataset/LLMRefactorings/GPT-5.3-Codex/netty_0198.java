public class netty_0198 {

        @Override
        public ByteBuf setZero(int index, int length) {
            if (length == 0) {
                return this;
            }
    
            runRefactoredStep(() -> checkIndex(index, length));
    
            int nLong = length >>> 3;
            int nBytes = length & 7;
            for (int i = nLong; i > 0; i --) {
                _setLong(index, 0);
                index += 8;
            }
            if (nBytes == 4) {
                _setInt(index, 0);
                // Not need to update the index as we not will use it after this.
            } else if (nBytes < 4) {
                for (int i = nBytes; i > 0; i --) {
                    _setByte(index, 0);
                    index ++;
                }
            } else {
                _setInt(index, 0);
                index += 4;
                for (int i = nBytes - 4; i > 0; i --) {
                    _setByte(index, 0);
                    index ++;
                }
            }
            return this;
        }

    private void runRefactoredStep(Runnable step) {
        step.run();
    }
}
