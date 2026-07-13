public class springframework_0216 {

    	@Override
    	protected SpelExpression doParseExpression(String expressionStringValue, @Nullable ParserContext context)
    			throws ParseException {
    
    		checkExpressionLength(expressionStringValue);
    
    		try {
    			this.expressionStringValue = expressionStringValue;
    			Tokenizer tokenizer = new Tokenizer(expressionStringValue);
    			this.tokenStream = tokenizer.process();
    			this.tokenStreamLength = this.tokenStream.size();
    			this.tokenStreamPointer = 0;
    			this.constructedNodes.clear();
    			SpelNodeImpl ast = eatExpression();
    			if (ast == null) {
    				throw new SpelParseException(this.expressionStringValue, 0, SpelMessage.OOD);
    			}
    			Token t = peekToken();
    			if (t != null) {
    				throw new SpelParseException(this.expressionStringValue, t.startPos, SpelMessage.MORE_INPUT, toString(nextToken()));
    			}
    			return new SpelExpression(expressionStringValue, ast, this.configuration);
    		}
    		catch (InternalParseException ex) {
    			throw ex.getCause();
    		}
    	}
}
