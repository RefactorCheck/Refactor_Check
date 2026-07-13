public class zxing_0072 {

      static void getNumDataBytesAndNumECBytesForBlockID(int numTotalBytes,
                                                         int numDataBytes,
                                                         int numRSBlocks,
                                                         int blockID,
                                                         int[] numDataBytesInBlock,
                                                         int[] numECBytesInBlock) throws WriterException {
        if (blockID >= numRSBlocks) {
          throw new WriterException("Block ID too large");
        }
        int numRsBlocksInGroup2 = numTotalBytes % numRSBlocks;
        int numRsBlocksInGroup1 = numRSBlocks - numRsBlocksInGroup2;
        int numTotalBytesInGroup1 = numTotalBytes / numRSBlocks;
        int numTotalBytesInGroup2 = numTotalBytesInGroup1 + 1;
        int numDataBytesInGroup1 = numDataBytes / numRSBlocks;
        int numDataBytesInGroup2 = numDataBytesInGroup1 + 1;
        int numEcBytesInGroup1 = numTotalBytesInGroup1 - numDataBytesInGroup1;
        int numEcBytesInGroup2 = numTotalBytesInGroup2 - numDataBytesInGroup2;
        validateBlockParameters(numTotalBytes, numDataBytes, numRSBlocks,
            numRsBlocksInGroup1, numRsBlocksInGroup2,
            numDataBytesInGroup1, numDataBytesInGroup2,
            numEcBytesInGroup1, numEcBytesInGroup2);

        if (blockID < numRsBlocksInGroup1) {
          numDataBytesInBlock[0] = numDataBytesInGroup1;
          numECBytesInBlock[0] = numEcBytesInGroup1;
        } else {
          numDataBytesInBlock[0] = numDataBytesInGroup2;
          numECBytesInBlock[0] = numEcBytesInGroup2;
        }
      }

      private static void validateBlockParameters(int numTotalBytes,
                                                   int numDataBytes,
                                                   int numRSBlocks,
                                                   int numRsBlocksInGroup1,
                                                   int numRsBlocksInGroup2,
                                                   int numDataBytesInGroup1,
                                                   int numDataBytesInGroup2,
                                                   int numEcBytesInGroup1,
                                                   int numEcBytesInGroup2) throws WriterException {
        if (numEcBytesInGroup1 != numEcBytesInGroup2) {
          throw new WriterException("EC bytes mismatch");
        }
        if (numRSBlocks != numRsBlocksInGroup1 + numRsBlocksInGroup2) {
          throw new WriterException("RS blocks mismatch");
        }
        if (numTotalBytes !=
            ((numDataBytesInGroup1 + numEcBytesInGroup1) *
                numRsBlocksInGroup1) +
                ((numDataBytesInGroup2 + numEcBytesInGroup2) *
                    numRsBlocksInGroup2)) {
          throw new WriterException("Total bytes mismatch");
        }
      }
}
