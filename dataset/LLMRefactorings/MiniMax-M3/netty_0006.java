public class netty_0006 {

        boolean write(final int value) {
            if (blockLength > blockLengthLimit) {
                return false;
            }
            final int rleCurrentValue = this.rleCurrentValue;
            final int rleLength = this.rleLength;
            final int rleCurrentByte = rleCurrentValue & 0xff;
    
            if (rleLength == 0) {
                this.rleCurrentValue = value;
                this.rleLength = 1;
            } else if (rleCurrentValue != value) {
                writeRun(rleCurrentByte, rleLength);
                this.rleCurrentValue = value;
                this.rleLength = 1;
            } else {
                if (rleLength == 254) {
                    writeRun(rleCurrentByte, 255);
                    this.rleLength = 0;
                } else {
                    this.rleLength = rleLength + 1;
                }
            }
            return true;
        }
}
