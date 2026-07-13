public class springframework_0291 {
    private int regexStart;


    	private void skipCaptureRegex() {
    		this.pos++;
    		this.regexStart = this.pos;
    		int curlyBracketDepth = 0; // how deep in nested {...} pairs
    		boolean previousBackslash = false;
    
    		while (this.pos < this.pathPatternLength) {
    			char ch = this.pathPatternData[this.pos];
    			if (ch == '\\' && !previousBackslash) {
    				this.pos++;
    				previousBackslash = true;
    				continue;
    			}
    			if (ch == '{' && !previousBackslash) {
    				curlyBracketDepth++;
    			}
    			else if (ch == '}' && !previousBackslash) {
    				if (curlyBracketDepth == 0) {
    					if (regexStart == this.pos) {
    						throw new PatternParseException(regexStart, this.pathPatternData,
    								PatternMessage.MISSING_REGEX_CONSTRAINT);
    					}
    					return;
    				}
    				curlyBracketDepth--;
    			}
    			if (ch == this.parser.getPathOptions().separator() && !previousBackslash) {
    				throw new PatternParseException(this.pos, this.pathPatternData,
    						PatternMessage.MISSING_CLOSE_CAPTURE);
    			}
    			this.pos++;
    			previousBackslash = false;
    		}
    
    		throw new PatternParseException(this.pos - 1, this.pathPatternData,
    				PatternMessage.MISSING_CLOSE_CAPTURE);
    	}
}
