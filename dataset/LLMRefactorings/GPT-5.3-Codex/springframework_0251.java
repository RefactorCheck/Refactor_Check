public class springframework_0251 {

    	@Override
    	public @Nullable Advisor getAdvisor(Method candidateAdviceMethod, MetadataAwareAspectInstanceFactory aspectInstanceFactory,
    			int declarationOrderInAspect, String aspectName) {
    
    		applyExtractedRefactoring();

    		AspectJExpressionPointcut expressionPointcut = getPointcut(
    				candidateAdviceMethod, aspectInstanceFactory.getAspectMetadata().getAspectClass());
    		if (expressionPointcut == null) {
    			return null;
    		}
    
    		try {
    			return new InstantiationModelAwarePointcutAdvisorImpl(expressionPointcut, candidateAdviceMethod,
    					this, aspectInstanceFactory, declarationOrderInAspect, aspectName);
    		}
    		catch (IllegalArgumentException | IllegalStateException ex) {
    			if (logger.isDebugEnabled()) {
    				logger.debug("Ignoring incompatible advice method: " + candidateAdviceMethod, ex);
    			}
    			return null;
    		}
    	}

	private void applyExtractedRefactoring() {
    		validate(aspectInstanceFactory.getAspectMetadata().getAspectClass());
	}
}
