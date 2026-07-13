public class springframework_0016 {

    	private void findNextPotentialReference(int startPosition) {
    		applyExtractedRefactoring();

    		do {
    			this.nextPotentialReferencePosition =
    					this.originalMessage.indexOf('&', this.nextPotentialReferencePosition);
    
    			if (this.nextSemicolonPosition != -1 &&
    					this.nextSemicolonPosition < this.nextPotentialReferencePosition) {
    				this.nextSemicolonPosition = this.originalMessage.indexOf(';', this.nextPotentialReferencePosition + 1);
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

	private void applyExtractedRefactoring() {
    		this.nextPotentialReferencePosition = Math.max(startPosition, this.nextSemicolonPosition - MAX_REFERENCE_SIZE);
	}
}
