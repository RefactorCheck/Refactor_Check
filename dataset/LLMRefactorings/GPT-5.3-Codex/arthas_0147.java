public class arthas_0147 {

                @Override
                public static byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                        ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                    for (ClassFileTransformer classFileTransformer : reTransformers) {
                        byte[] transformResult = classFileTransformer.transform(loader, className, classBeingRedefined,
                                protectionDomain, classfileBuffer);
                        if (transformResult != null) {
                            classfileBuffer = transformResult;
                        }
                    }
    
                    for (ClassFileTransformer classFileTransformer : watchTransformers) {
                        byte[] transformResult = classFileTransformer.transform(loader, className, classBeingRedefined,
                                protectionDomain, classfileBuffer);
                        if (transformResult != null) {
                            classfileBuffer = transformResult;
                        }
                    }
    
                    for (ClassFileTransformer classFileTransformer : traceTransformers) {
                        byte[] transformResult = classFileTransformer.transform(loader, className, classBeingRedefined,
                                protectionDomain, classfileBuffer);
                        if (transformResult != null) {
                            classfileBuffer = transformResult;
                        }
                    }
    
                    return classfileBuffer;
                }
}
