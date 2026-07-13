public class zxing_0072 {

      static void getNumDataBytesAndNumECBytesForBlockIDRefactored(int numTotalBytes,
                                                         int numDataBytes,
                                                         int numRSBlocks,
                                                         int blockID,
                                                         int[] numDataBytesInBlock,
                                                         int[] numECBytesInBlock) throws WriterException {
        if (blockID >= numRSBlocks) {
          throw new WriterException("Block ID too large");
        }
        // numRsBlocksInGroup2 = 196 % 5 = 1
        int numRsBlocksInGroup2 = numTotalBytes % numRSBlocks;
        // numRsBlocksInGroup1 = 5 - 1 = 4
        int numRsBlocksInGroup1 = numRSBlocks - numRsBlocksInGroup2;
        // numTotalBytesInGroup1 = 196 / 5 = 39
        int numTotalBytesInGroup1 = numTotalBytes / numRSBlocks;
        // numTotalBytesInGroup2 = 39 + 1 = 40
        int numTotalBytesInGroup2 = numTotalBytesInGroup1 + 1;
        // numDataBytesInGroup1 = 66 / 5 = 13
        int numDataBytesInGroup1 = numDataBytes / numRSBlocks;
        // numDataBytesInGroup2 = 13 + 1 = 14
        int numDataBytesInGroup2 = numDataBytesInGroup1 + 1;
        // numEcBytesInGroup1 = 39 - 13 = 26
        int numEcBytesInGroup1 = numTotalBytesInGroup1 - numDataBytesInGroup1;
        // numEcBytesInGroup2 = 40 - 14 = 26
        int numEcBytesInGroup2 = numTotalBytesInGroup2 - numDataBytesInGroup2;
        // Sanity checks.
        // 26 = 26
        if (numEcBytesInGroup1 != numEcBytesInGroup2) {
          throw new WriterException("EC bytes mismatch");
        }
        // 5 = 4 + 1.
        if (numRSBlocks != numRsBlocksInGroup1 + numRsBlocksInGroup2) {
          throw new WriterException("RS blocks mismatch");
        }
        // 196 = (13 + 26) * 4 + (14 + 26) * 1
        if (numTotalBytes !=
            ((numDataBytesInGroup1 + numEcBytesInGroup1) *
                numRsBlocksInGroup1) +
                ((numDataBytesInGroup2 + numEcBytesInGroup2) *
                    numRsBlocksInGroup2)) {
          throw new WriterException("Total bytes mismatch");
        }
    
        if (blockID < numRsBlocksInGroup1) {
          numDataBytesInBlock[0] = numDataBytesInGroup1;
          numECBytesInBlock[0] = numEcBytesInGroup1;
        } else {
          numDataBytesInBlock[0] = numDataBytesInGroup2;
          numECBytesInBlock[0] = numEcBytesInGroup2;
        }
      }
}
