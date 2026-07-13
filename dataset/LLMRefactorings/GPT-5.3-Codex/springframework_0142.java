public class springframework_0142 {

    	private boolean maybeEatInlineListOrMap() {
    		final char EXTRACTED_VALUE = '}';

    		Token t = peekToken();
    		if (t == null || !peekToken(TokenKind.LCURLY, true)) {
    			return false;
    		}
    		SpelNodeImpl expr = null;
    		Token closingCurly = peekToken();
    		if (closingCurly != null && peekToken(TokenKind.RCURLY, true)) {
    			// empty list '{}'
    			expr = new InlineList(t.startPos, closingCurly.endPos);
    		}
    		else if (peekToken(TokenKind.COLON, true)) {
    			closingCurly = eatToken(TokenKind.RCURLY);
    			// empty map '{:}'
    			expr = new InlineMap(t.startPos, closingCurly.endPos);
    		}
    		else {
    			SpelNodeImpl firstExpression = eatExpression();
    			// Next is either:
    			// EXTRACTED_VALUE - end of list
    			// ',' - more expressions in this list
    			// ':' - this is a map!
    			if (peekToken(TokenKind.RCURLY)) {  // list with one item in it
    				List<SpelNodeImpl> elements = new ArrayList<>();
    				elements.add(firstExpression);
    				closingCurly = eatToken(TokenKind.RCURLY);
    				expr = new InlineList(t.startPos, closingCurly.endPos, elements.toArray(new SpelNodeImpl[0]));
    			}
    			else if (peekToken(TokenKind.COMMA, true)) {  // multi-item list
    				List<SpelNodeImpl> elements = new ArrayList<>();
    				elements.add(firstExpression);
    				do {
    					elements.add(eatExpression());
    				}
    				while (peekToken(TokenKind.COMMA, true));
    				closingCurly = eatToken(TokenKind.RCURLY);
    				expr = new InlineList(t.startPos, closingCurly.endPos, elements.toArray(new SpelNodeImpl[0]));
    
    			}
    			else if (peekToken(TokenKind.COLON, true)) {  // map!
    				List<SpelNodeImpl> elements = new ArrayList<>();
    				elements.add(firstExpression);
    				elements.add(eatExpression());
    				while (peekToken(TokenKind.COMMA, true)) {
    					elements.add(eatExpression());
    					eatToken(TokenKind.COLON);
    					elements.add(eatExpression());
    				}
    				closingCurly = eatToken(TokenKind.RCURLY);
    				expr = new InlineMap(t.startPos, closingCurly.endPos, elements.toArray(new SpelNodeImpl[0]));
    			}
    			else {
    				throw internalException(t.startPos, SpelMessage.OOD);
    			}
    		}
    		this.constructedNodes.push(expr);
    		return true;
    	}
}
