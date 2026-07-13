public class zxing_0254 {

    private static final int LATCH_B256 = 231;
    private static final int LATCH_C40 = 230;
    private static final int LATCH_TEXT = 239;
    private static final int LATCH_X12 = 238;
    private static final int LATCH_EDF = 240;
    private static final int LATCH_ASCII = 254;

    byte[] getLatchBytes() {
        switch (getPreviousMode()) {
            case ASCII:
            case B256:
                switch (mode) {
                    case B256:
                        return getBytes(LATCH_B256);
                    case C40:
                        return getBytes(LATCH_C40);
                    case TEXT:
                        return getBytes(LATCH_TEXT);
                    case X12:
                        return getBytes(LATCH_X12);
                    case EDF:
                        return getBytes(LATCH_EDF);
                }
                break;
            case C40:
            case TEXT:
            case X12:
                if (mode != getPreviousMode()) {
                    switch (mode) {
                        case ASCII:
                            return getBytes(LATCH_ASCII);
                        case B256:
                            return getBytes(LATCH_ASCII, LATCH_B256);
                        case C40:
                            return getBytes(LATCH_ASCII, LATCH_C40);
                        case TEXT:
                            return getBytes(LATCH_ASCII, LATCH_TEXT);
                        case X12:
                            return getBytes(LATCH_ASCII, LATCH_X12);
                        case EDF:
                            return getBytes(LATCH_ASCII, LATCH_EDF);
                    }
                }
                break;
            case EDF:
                assert mode == Mode.EDF;
                break;
        }
        return new byte[0];
    }
}
