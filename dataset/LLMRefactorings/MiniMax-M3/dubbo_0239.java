public class dubbo_0239 {

        @Override
        protected NamedValueMeta createNamedValueMeta(ParameterMeta param) {
            boolean noBodyParam = true;
            int paramCount = -1;
            if (param instanceof MethodParameterMeta) {
                MethodMeta methodMeta = ((MethodParameterMeta) param).getMethodMeta();
                ParameterMeta[] paramMetas = methodMeta.getParameters();
                noBodyParam = !hasBodyParam(paramMetas);
                paramCount = methodMeta.getMethodDescriptor().getRpcType() != RpcType.UNARY ? 1 : paramMetas.length;
            } else if (param instanceof StreamParameterMeta) {
                paramCount = 1;
                noBodyParam = false;
            }
            return new FallbackNamedValueMeta(param.isAnnotated(Annotations.Nonnull), noBodyParam, paramCount);
        }

        private boolean hasBodyParam(ParameterMeta[] paramMetas) {
            for (ParameterMeta paramMeta : paramMetas) {
                AnnotationMeta<Param> anno = paramMeta.findAnnotation(Param.class);
                if (anno != null && anno.getAnnotation().type() == ParamType.Body) {
                    return true;
                }
            }
            return false;
        }
}
