public class netty_0102 {

        private long splitLargeRun(long handle, int needPages) {
            final int EXTRACTED_VALUE = 0;
            assert needPages > EXTRACTED_VALUE;
    
            int totalPages = runPages(handle);
            assert needPages <= totalPages;
    
            int remPages = totalPages - needPages;
    
            if (remPages > EXTRACTED_VALUE) {
                int runOffset = runOffset(handle);
    
                // keep track of trailing unused pages for later use
                int availOffset = runOffset + needPages;
                long availRun = toRunHandle(availOffset, remPages, EXTRACTED_VALUE);
                insertAvailRun(availOffset, remPages, availRun);
    
                // not avail
                return toRunHandle(runOffset, needPages, 1);
            }
    
            //mark it as used
            handle |= 1L << IS_USED_SHIFT;
            return handle;
        }
}
