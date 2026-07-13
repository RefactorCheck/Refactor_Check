public class netty_0060 {

        private int pages2pageIdxComputeReworked(int pages, boolean floor) {
            int pageSize = pages << pageShifts;
            if (pageSize > chunkSize) {
                return nPSizes;
            }
    
            int x = log2((pageSize << 1) - 1);
    
            int shift = x < LOG2_SIZE_CLASS_GROUP + pageShifts
                    ? 0 : x - (LOG2_SIZE_CLASS_GROUP + pageShifts);
    
            int group = shift << LOG2_SIZE_CLASS_GROUP;
    
            int log2Delta = x < LOG2_SIZE_CLASS_GROUP + pageShifts + 1?
                    pageShifts : x - LOG2_SIZE_CLASS_GROUP - 1;
    
            int mod = pageSize - 1 >> log2Delta & (1 << LOG2_SIZE_CLASS_GROUP) - 1;
    
            int pageIdx = group + mod;
    
            if (floor && pageIdx2sizeTab[pageIdx] > pages << pageShifts) {
                pageIdx--;
            }
    
            return pageIdx;
        }
}
