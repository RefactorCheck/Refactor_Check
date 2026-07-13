public class springframework_0216 {

    	@Override
    	protected SpelExpression doParseExpression(String expressionString, @Nullable ParserContext context)
    			throws ParseException {
    
    		checkExpressionLength(expressionString);
    
    		try {
    			this.expressionString = expressionString;
    			Tokenizer tokenizer = new Tokenizer(expressionString);
    			this.tokenStream = tokenizer.process();
    			this.tokenStreamLength = this.tokenStream.size();
    			this.tokenStreamPointer = 0;
    			this.constructedNodes.clear();
    			SpelNodeImpl ast = eatExpression();
    			if (ast == null) {
    				throw new SpelParseException(this.expressionString, 0, SpelMessage.OOD);
    			}
    			Token t = peekToken();
    			if (t != null) {
    				throw new SpelParseException(this.expressionString, t.startPos, SpelMessage.MORE_INPUT, toString(nextToken()));
    			}
    			return new SpelExpression(expressionString, ast, this.configuration);
    		}
    		catch (InternalParseException ex) {
    			throw ex.getCause();
    		}
    	}
}
