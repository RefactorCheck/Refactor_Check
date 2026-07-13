public class arthas_0295 {

    private static final int BITS_PER_BYTE = 8;
    private static final int INITIAL_BIT_SHIFT = 24;
    private static final int MAX_BYTE_VALUE = 0xFF;
    private static final int BYTE_MODULO = 0x100;
    private static final int COMPOUND_WINDOW_SHIFT = 0x10000;

    @Override
    public int[] startSubnegotiationLocal()
    {
        int nCompoundWindowSize = m_nWidth * COMPOUND_WINDOW_SHIFT + m_nHeight;
        int nResponseSize = 5;
        int nIndex;
        int nShift;
        int nTurnedOnBits;

        if ((m_nWidth % BYTE_MODULO) == MAX_BYTE_VALUE) {
            nResponseSize += 1;
        }

        if ((m_nWidth / BYTE_MODULO) == MAX_BYTE_VALUE) {
            nResponseSize += 1;
        }

        if ((m_nHeight % BYTE_MODULO) == MAX_BYTE_VALUE) {
            nResponseSize += 1;
        }

        if ((m_nHeight / BYTE_MODULO) == MAX_BYTE_VALUE) {
            nResponseSize += 1;
        }

        int response[] = new int[nResponseSize];

        response[0] = WINDOW_SIZE;

        for (
            nIndex=1, nShift = INITIAL_BIT_SHIFT;
            nIndex < nResponseSize;
            nIndex++, nShift -= BITS_PER_BYTE
        ) {
            nTurnedOnBits = MAX_BYTE_VALUE;
            nTurnedOnBits <<= nShift;
            response[nIndex] = (nCompoundWindowSize & nTurnedOnBits) >>> nShift;

            if (response[nIndex] == MAX_BYTE_VALUE) {
                nIndex++;
                response[nIndex] = MAX_BYTE_VALUE;
            }
        }

        return response;
    }
}
