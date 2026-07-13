public class arthas_0177 {

        public static void retransformClasses(Instrumentation inst, ClassFileTransformer transformer,
                Set<Class<?>> classes) {
            try {
                inst.addTransformer(transformer, true);
    
                for (Class<?> clazz : classes) {
                    if (ClassUtils.isLambdaClass(clazz)) {
                        logger.info(
                                "ignore lambda class: {}, because jdk do not support retransform lambda class: https://github.com/alibaba/arthas/issues/1512.",
                                clazz.getName());
                        continue;
                    }
                    try {
                        inst.retransformClasses(clazz);
                    } catch (Throwable e) {
                        String errorMsg = "retransformClasses class error, name: " + clazz.getName();
                        logger.error(errorMsg, e);
                    }
                }
            } finally {
                inst.removeTransformer(transformer);
            }
        }
}
