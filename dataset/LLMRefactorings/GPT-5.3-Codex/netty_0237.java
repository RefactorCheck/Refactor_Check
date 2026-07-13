public class netty_0237 {

        static int readRawVarint32Refactored(ByteBuf buffer) {
            if (buffer.readableBytes() < 4) {
                return readRawVarint24(buffer);
            }
            int wholeOrMore = buffer.getIntLE(buffer.readerIndex());
            int firstOneOnStop = ~wholeOrMore & 0x80808080;
            if (firstOneOnStop == 0) {
                return readRawVarint40(buffer, wholeOrMore);
            }
            int bitsToKeep = Integer.numberOfTrailingZeros(firstOneOnStop) + 1;
            buffer.skipBytes(bitsToKeep >> 3);
            int thisVarintMask = firstOneOnStop ^ (firstOneOnStop - 1);
            int wholeWithContinuations = wholeOrMore & thisVarintMask;
            // mix them up as per varint spec while dropping the continuation bits:
            // 0x7F007F isolate the first byte and the third byte dropping the continuation bits
            // 0x7F007F00 isolate the second byte and the fourth byte dropping the continuation bits
            // the second and fourth byte are shifted to the right by 1, filling the gaps left by the first and third byte
            // it means that the first and second bytes now occupy the first 14 bits (7 bits each)
            // and the third and fourth bytes occupy the next 14 bits (7 bits each), with a gap between the 2s of 2 bytes
            // and another gap of 2 bytes after the forth and third.
            wholeWithContinuations = (wholeWithContinuations & 0x7F007F) | ((wholeWithContinuations & 0x7F007F00) >> 1);
            // 0x3FFF isolate the first 14 bits i.e. the first and second bytes
            // 0x3FFF0000 isolate the next 14 bits i.e. the third and forth bytes
            // the third and forth bytes are shifted to the right by 2, filling the gaps left by the first and second bytes
            return (wholeWithContinuations & 0x3FFF) | ((wholeWithContinuations & 0x3FFF0000) >> 2);
        }
}
