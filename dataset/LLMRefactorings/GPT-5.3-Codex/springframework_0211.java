public class springframework_0211 {

    	private boolean maybeEatLiteral() {

    		if ((peekToken()) == null) {
    			return false;
    		}
    		if ((peekToken()).kind == TokenKind.LITERAL_INT) {
    			push(Literal.getIntLiteral((peekToken()).stringValue(), (peekToken()).startPos, (peekToken()).endPos, 10));
    		}
    		else if ((peekToken()).kind == TokenKind.LITERAL_LONG) {
    			push(Literal.getLongLiteral((peekToken()).stringValue(), (peekToken()).startPos, (peekToken()).endPos, 10));
    		}
    		else if ((peekToken()).kind == TokenKind.LITERAL_HEXINT) {
    			push(Literal.getIntLiteral((peekToken()).stringValue(), (peekToken()).startPos, (peekToken()).endPos, 16));
    		}
    		else if ((peekToken()).kind == TokenKind.LITERAL_HEXLONG) {
    			push(Literal.getLongLiteral((peekToken()).stringValue(), (peekToken()).startPos, (peekToken()).endPos, 16));
    		}
    		else if ((peekToken()).kind == TokenKind.LITERAL_REAL) {
    			push(Literal.getRealLiteral((peekToken()).stringValue(), (peekToken()).startPos, (peekToken()).endPos, false));
    		}
    		else if ((peekToken()).kind == TokenKind.LITERAL_REAL_FLOAT) {
    			push(Literal.getRealLiteral((peekToken()).stringValue(), (peekToken()).startPos, (peekToken()).endPos, true));
    		}
    		else if (peekIdentifierToken("true")) {
    			push(new BooleanLiteral((peekToken()).stringValue(), (peekToken()).startPos, (peekToken()).endPos, true));
    		}
    		else if (peekIdentifierToken("false")) {
    			push(new BooleanLiteral((peekToken()).stringValue(), (peekToken()).startPos, (peekToken()).endPos, false));
    		}
    		else if ((peekToken()).kind == TokenKind.LITERAL_STRING) {
    			push(new StringLiteral((peekToken()).stringValue(), (peekToken()).startPos, (peekToken()).endPos, (peekToken()).stringValue()));
    		}
    		else {
    			return false;
    		}
    		nextToken();
    		return true;
    	}
}
