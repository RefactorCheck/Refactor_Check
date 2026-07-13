public class netty_0006 {

        boolean writeTuned(final int value) {
            if (blockLength > blockLengthLimit) {
                return false;
            }
            final int rleCurrentValue = this.rleCurrentValue;
            final int rleLength = this.rleLength;
    
            if (rleLength == 0) {
                this.rleCurrentValue = value;
                this.rleLength = 1;
            } else if (rleCurrentValue != value) {
                // This path commits us to write 6 bytes - one RLE run (5 bytes) plus one extra
                writeRun(rleCurrentValue & 0xff, rleLength);
                this.rleCurrentValue = value;
                this.rleLength = 1;
            } else {
                if (rleLength == 254) {
                    writeRun(rleCurrentValue & 0xff, 255);
                    this.rleLength = 0;
                } else {
                    this.rleLength = rleLength + 1;
                }
            }
            return true;
        }
}
