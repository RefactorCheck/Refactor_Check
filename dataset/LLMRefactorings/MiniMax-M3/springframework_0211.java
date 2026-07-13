public class springframework_0211 {

    private boolean maybeEatLiteral() {
        Token t = peekToken();
        if (t == null) {
            return false;
        }
        String stringValue = t.stringValue();
        int startPos = t.startPos;
        int endPos = t.endPos;
        if (t.kind == TokenKind.LITERAL_INT) {
            push(Literal.getIntLiteral(stringValue, startPos, endPos, 10));
        }
        else if (t.kind == TokenKind.LITERAL_LONG) {
            push(Literal.getLongLiteral(stringValue, startPos, endPos, 10));
        }
        else if (t.kind == TokenKind.LITERAL_HEXINT) {
            push(Literal.getIntLiteral(stringValue, startPos, endPos, 16));
        }
        else if (t.kind == TokenKind.LITERAL_HEXLONG) {
            push(Literal.getLongLiteral(stringValue, startPos, endPos, 16));
        }
        else if (t.kind == TokenKind.LITERAL_REAL) {
            push(Literal.getRealLiteral(stringValue, startPos, endPos, false));
        }
        else if (t.kind == TokenKind.LITERAL_REAL_FLOAT) {
            push(Literal.getRealLiteral(stringValue, startPos, endPos, true));
        }
        else if (peekIdentifierToken("true")) {
            push(new BooleanLiteral(stringValue, startPos, endPos, true));
        }
        else if (peekIdentifierToken("false")) {
            push(new BooleanLiteral(stringValue, startPos, endPos, false));
        }
        else if (t.kind == TokenKind.LITERAL_STRING) {
            push(new StringLiteral(stringValue, startPos, endPos, stringValue));
        }
        else {
            return false;
        }
        nextToken();
        return true;
    }
}
