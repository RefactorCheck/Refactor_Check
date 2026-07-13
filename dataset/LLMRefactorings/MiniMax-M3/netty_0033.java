public class netty_0033 {

    private void writeRun(final int value, int runLength) {
        final int blockLength = this.blockLength;
        final byte[] block = this.block;

        blockValuesPresent[value] = true;
        crc.updateCRC(value, runLength);

        final byte byteValue = (byte) value;
        switch (runLength) {
            case 1:
                writeBytes(block, blockLength, byteValue, 1);
                this.blockLength = blockLength + 1;
                break;
            case 2:
                writeBytes(block, blockLength, byteValue, 2);
                this.blockLength = blockLength + 2;
                break;
            case 3:
                writeBytes(block, blockLength, byteValue, 3);
                this.blockLength = blockLength + 3;
                break;
            default:
                runLength -= 4;
                blockValuesPresent[runLength] = true;
                writeBytes(block, blockLength, byteValue, 4);
                block[blockLength + 4] = (byte) runLength;
                this.blockLength = blockLength + 5;
                break;
        }
    }

    private static void writeBytes(byte[] block, int offset, byte value, int count) {
        for (int i = 0; i < count; i++) {
            block[offset + i] = value;
        }
    }
}
