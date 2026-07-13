public class arthas_0295 {

        @Override
        public int[] startSubnegotiationLocal()
        {
            int nCompoundWindowSize = m_nWidth * 0x10000 + m_nHeight;
            int nResponseSize = 5;
            int nIndex;
            int nShift;
            int nTurnedOnBits;
    
            if ((m_nWidth % 0x100) == 0xFF) {
                nResponseSize += 1;
            }
    
            if ((m_nWidth / 0x100) == 0xFF) {
                nResponseSize += 1;
            }
    
            if ((m_nHeight % 0x100) == 0xFF) {
                nResponseSize += 1;
            }
    
            if ((m_nHeight / 0x100) == 0xFF) {
                nResponseSize += 1;
            }
    
            //
            // allocate response array
            //
            int response[] = new int[nResponseSize];
    
            //
            // Build response array.
            // ---------------------
            // 1. put option name.
            // 2. loop through Window size and fill the values,
            // 3.    duplicate 'ff' if needed.
            //
    
            response[0] = WINDOW_SIZE;                          // 1 //
    
            for (                                               // 2 //
                nIndex=1, nShift = 24;
                nIndex < nResponseSize;
                nIndex++, nShift -=8
            ) {
                nTurnedOnBits = 0xFF;
                nTurnedOnBits <<= nShift;
                response[nIndex] = (nCompoundWindowSize & nTurnedOnBits) >>> nShift;
    
                if (response[nIndex] == 0xff) {                 // 3 //
                    nIndex++;
                    response[nIndex] = 0xff;
                }
            }
    
            return response;
        }
}
