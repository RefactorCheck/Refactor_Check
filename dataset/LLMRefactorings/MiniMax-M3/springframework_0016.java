public class springframework_0016 {

    private static final char AMPERSAND = '&';
    private static final char SEMICOLON = ';';

    private void findNextPotentialReference(int startPosition) {
        this.nextPotentialReferencePosition = Math.max(startPosition, this.nextSemicolonPosition - MAX_REFERENCE_SIZE);

        do {
            this.nextPotentialReferencePosition =
                    this.originalMessage.indexOf(AMPERSAND, this.nextPotentialReferencePosition);

            if (this.nextSemicolonPosition != -1 &&
                    this.nextSemicolonPosition < this.nextPotentialReferencePosition) {
                this.nextSemicolonPosition = this.originalMessage.indexOf(SEMICOLON, this.nextPotentialReferencePosition + 1);
            }

            if (this.nextPotentialReferencePosition == -1) {
                break;
            }
            if (this.nextSemicolonPosition == -1) {
                this.nextPotentialReferencePosition = -1;
                break;
            }
            if (this.nextSemicolonPosition - this.nextPotentialReferencePosition < MAX_REFERENCE_SIZE) {
                break;
            }

            this.nextPotentialReferencePosition = this.nextPotentialReferencePosition + 1;
        }
        while (this.nextPotentialReferencePosition != -1);
    }
}
