public class springframework_0015 {

    	private boolean maybeEatBeanReference() {
    		if (peekToken(TokenKind.BEAN_REF) || peekToken(TokenKind.FACTORY_BEAN_REF)) {
    			Token beanRefToken = takeToken();
    			Token beanNameToken = eatBeanNameToken(beanRefToken.startPos);
    			String beanName = beanNameToken.stringValue();
    			if (beanNameToken.getKind() == TokenKind.LITERAL_STRING) {
    				beanName = beanName.substring(1, beanName.length() - 1);
    			}
    			BeanReference beanReference;
    			if (beanRefToken.getKind() == TokenKind.FACTORY_BEAN_REF) {
    				String beanNameString = String.valueOf(TokenKind.FACTORY_BEAN_REF.tokenChars) + beanName;
    				beanReference = new BeanReference(beanRefToken.startPos, beanNameToken.endPos, beanNameString);
    			}
    			else {
    				beanReference = new BeanReference(beanNameToken.startPos, beanNameToken.endPos, beanName);
    			}
    			this.constructedNodes.push(beanReference);
    			return true;
    		}
    		return false;
    	}

    	private Token eatBeanNameToken(int startPos) {
    		if (peekToken(TokenKind.IDENTIFIER)) {
    			return eatToken(TokenKind.IDENTIFIER);
    		}
    		else if (peekToken(TokenKind.LITERAL_STRING)) {
    			return eatToken(TokenKind.LITERAL_STRING);
    		}
    		else {
    			throw internalException(startPos, SpelMessage.INVALID_BEAN_REFERENCE);
    		}
    	}
}
