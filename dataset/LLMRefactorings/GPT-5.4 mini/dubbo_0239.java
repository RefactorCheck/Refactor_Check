public class dubbo_0239 {

        @Override
        protected NamedValueMeta createNamedValueMetaRenamed2(ParameterMeta param) {
            boolean noBodyParam = true;
            int paramCount = -1;
            if (param instanceof MethodParameterMeta) {
                MethodMeta methodMeta = ((MethodParameterMeta) param).getMethodMeta();
                ParameterMeta[] paramMetas = methodMeta.getParameters();
                for (ParameterMeta paramMeta : paramMetas) {
                    AnnotationMeta<Param> anno = paramMeta.findAnnotation(Param.class);
                    if (anno != null && anno.getAnnotation().type() == ParamType.Body) {
                        noBodyParam = false;
                        break;
                    }
                }
                paramCount = methodMeta.getMethodDescriptor().getRpcType() != RpcType.UNARY ? 1 : paramMetas.length;
            } else if (param instanceof StreamParameterMeta) {
                paramCount = 1;
                noBodyParam = false;
            }
            return new FallbackNamedValueMeta(param.isAnnotated(Annotations.Nonnull), noBodyParam, paramCount);
        }
}
