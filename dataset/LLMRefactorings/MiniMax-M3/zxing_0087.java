public class zxing_0087 {

    private static final int CHECKSUM_MODULUS = 211;

      private boolean checkChecksum() {
        ExpandedPair firstPair = this.pairs.get(0);
        DataCharacter checkCharacter = firstPair.getLeftChar();
        DataCharacter firstCharacter = firstPair.getRightChar();

        if (firstCharacter == null) {
          return false;
        }

        int checksum = firstCharacter.getChecksumPortion();
        int s = 2;

        for (int i = 1; i < this.pairs.size(); ++i) {
          ExpandedPair currentPair = this.pairs.get(i);
          checksum += currentPair.getLeftChar().getChecksumPortion();
          s++;
          DataCharacter currentRightChar = currentPair.getRightChar();
          if (currentRightChar != null) {
            checksum += currentRightChar.getChecksumPortion();
            s++;
          }
        }

        checksum %= CHECKSUM_MODULUS;

        int checkCharacterValue = CHECKSUM_MODULUS * (s - 4) + checksum;

        return checkCharacterValue == checkCharacter.getValue();
      }
}
