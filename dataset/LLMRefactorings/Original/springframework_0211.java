public class springframework_0211 {

    	private boolean maybeEatLiteral() {
    		Token t = peekToken();
    		if (t == null) {
    			return false;
    		}
    		if (t.kind == TokenKind.LITERAL_INT) {
    			push(Literal.getIntLiteral(t.stringValue(), t.startPos, t.endPos, 10));
    		}
    		else if (t.kind == TokenKind.LITERAL_LONG) {
    			push(Literal.getLongLiteral(t.stringValue(), t.startPos, t.endPos, 10));
    		}
    		else if (t.kind == TokenKind.LITERAL_HEXINT) {
    			push(Literal.getIntLiteral(t.stringValue(), t.startPos, t.endPos, 16));
    		}
    		else if (t.kind == TokenKind.LITERAL_HEXLONG) {
    			push(Literal.getLongLiteral(t.stringValue(), t.startPos, t.endPos, 16));
    		}
    		else if (t.kind == TokenKind.LITERAL_REAL) {
    			push(Literal.getRealLiteral(t.stringValue(), t.startPos, t.endPos, false));
    		}
    		else if (t.kind == TokenKind.LITERAL_REAL_FLOAT) {
    			push(Literal.getRealLiteral(t.stringValue(), t.startPos, t.endPos, true));
    		}
    		else if (peekIdentifierToken("true")) {
    			push(new BooleanLiteral(t.stringValue(), t.startPos, t.endPos, true));
    		}
    		else if (peekIdentifierToken("false")) {
    			push(new BooleanLiteral(t.stringValue(), t.startPos, t.endPos, false));
    		}
    		else if (t.kind == TokenKind.LITERAL_STRING) {
    			push(new StringLiteral(t.stringValue(), t.startPos, t.endPos, t.stringValue()));
    		}
    		else {
    			return false;
    		}
    		nextToken();
    		return true;
    	}
}
