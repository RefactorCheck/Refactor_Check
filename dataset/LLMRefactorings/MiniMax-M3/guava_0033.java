public class guava_0033 {

    private static long readUnsignedByte(ByteBuffer bb, int index, int shift) {
        return (long) toUnsignedInt(bb.get(index)) << shift;
    }

        @Override
        protected void processRemaining(ByteBuffer bb) {
          long k1 = 0;
          long k2 = 0;
          length += bb.remaining();
          switch (bb.remaining()) {
            case 15:
              k2 ^= readUnsignedByte(bb, 14, 48); // fall through
            case 14:
              k2 ^= readUnsignedByte(bb, 13, 40); // fall through
            case 13:
              k2 ^= readUnsignedByte(bb, 12, 32); // fall through
            case 12:
              k2 ^= readUnsignedByte(bb, 11, 24); // fall through
            case 11:
              k2 ^= readUnsignedByte(bb, 10, 16); // fall through
            case 10:
              k2 ^= readUnsignedByte(bb, 9, 8); // fall through
            case 9:
              k2 ^= (long) toUnsignedInt(bb.get(8)); // fall through
            case 8:
              k1 ^= bb.getLong();
              break;
            case 7:
              k1 ^= readUnsignedByte(bb, 6, 48); // fall through
            case 6:
              k1 ^= readUnsignedByte(bb, 5, 40); // fall through
            case 5:
              k1 ^= readUnsignedByte(bb, 4, 32); // fall through
            case 4:
              k1 ^= readUnsignedByte(bb, 3, 24); // fall through
            case 3:
              k1 ^= readUnsignedByte(bb, 2, 16); // fall through
            case 2:
              k1 ^= readUnsignedByte(bb, 1, 8); // fall through
            case 1:
              k1 ^= (long) toUnsignedInt(bb.get(0));
              break;
            default:
              throw new AssertionError("Should never get here.");
          }
          h1 ^= mixK1(k1);
          h2 ^= mixK2(k2);
        }
}
