public class springframework_0251 {

    @Override
    public @Nullable Advisor getAdvisor(Method candidateAdviceMethod, MetadataAwareAspectInstanceFactory aspectInstanceFactory,
            int declarationOrderInAspect, String aspectName) {

        validate(aspectInstanceFactory.getAspectMetadata().getAspectClass());

        AspectJExpressionPointcut expressionPointcut = getPointcut(
                candidateAdviceMethod, aspectInstanceFactory.getAspectMetadata().getAspectClass());
        if (expressionPointcut == null) {
            return null;
        }

        return instantiateAdvisor(expressionPointcut, candidateAdviceMethod, aspectInstanceFactory,
                declarationOrderInAspect, aspectName);
    }

    private @Nullable Advisor instantiateAdvisor(AspectJExpressionPointcut expressionPointcut, Method candidateAdviceMethod,
            MetadataAwareAspectInstanceFactory aspectInstanceFactory, int declarationOrderInAspect, String aspectName) {
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
}
