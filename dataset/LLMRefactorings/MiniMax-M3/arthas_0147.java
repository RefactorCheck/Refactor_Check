public class arthas_0147 {

                @Override
                public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                        ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
                    classfileBuffer = applyTransformers(reTransformers, loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
                    classfileBuffer = applyTransformers(watchTransformers, loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
                    classfileBuffer = applyTransformers(traceTransformers, loader, className, classBeingRedefined, protectionDomain, classfileBuffer);
                    return classfileBuffer;
                }

                private byte[] applyTransformers(List<ClassFileTransformer> transformers, ClassLoader loader, String className, 
                        Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {
                    for (ClassFileTransformer classFileTransformer : transformers) {
                        byte[] transformResult = classFileTransformer.transform(loader, className, classBeingRedefined,
                                protectionDomain, classfileBuffer);
                        if (transformResult != null) {
                            classfileBuffer = transformResult;
                        }
                    }
                    return classfileBuffer;
                }
}
