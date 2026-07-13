public class netty_0033 {

        private void writeRun(final int value, int runLength) {
            final int blockLength = this.blockLength;
            final byte[] block = this.block;
    
            blockValuesPresent[value] = true;
            crc.updateCRC(value, runLength);
    
            final byte byteValue = (byte) value;
            switch (runLength) {
                case 1:
                    block[blockLength] = byteValue;
                    this.blockLength = blockLength + 1;
                    break;
                case 2:
                    block[blockLength] = byteValue;
                    block[blockLength + 1] = byteValue;
                    this.blockLength = blockLength + 2;
                    break;
                case 3:
                    block[blockLength] = byteValue;
                    block[blockLength + 1] = byteValue;
                    block[blockLength + 2] = byteValue;
                    this.blockLength = blockLength + 3;
                    break;
                default:
                    runLength -= 4;
                    blockValuesPresent[runLength] = true;
                    block[blockLength] = byteValue;
                    block[blockLength + 1] = byteValue;
                    block[blockLength + 2] = byteValue;
                    block[blockLength + 3] = byteValue;
                    block[blockLength + 4] = (byte) runLength;
                    this.blockLength = blockLength + 5;
                    break;
            }
        }
}
