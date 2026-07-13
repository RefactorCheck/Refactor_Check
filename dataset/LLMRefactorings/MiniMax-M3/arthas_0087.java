public class arthas_0087 {

        @Override
        public void draw(CommandProcess process, SearchMethodModel result) {
            if (result.getMatchedClassLoaders() != null) {
                process.write("Matched classloaders: \n");
                ClassLoaderView.drawClassLoaders(process, result.getMatchedClassLoaders(), false);
                process.write("\n");
                return;
            }
    
            boolean detail = result.isDetail();
            MethodVO methodInfo = result.getMethodInfo();
    
            if (detail) {
                if (methodInfo.isConstructor()) {
                    process.write(RenderUtil.render(ClassUtils.renderConstructor(methodInfo), process.width()) + "\n");
                } else {
                    process.write(RenderUtil.render(ClassUtils.renderMethod(methodInfo), process.width()) + "\n");
                }
            } else {
                process.write(methodInfo.getDeclaringClass())
                        .write(" ")
                        .write(methodInfo.getMethodName())
                        .write(methodInfo.getDescriptor())
                        .write("\n");
            }
    
            String declaringClass = methodInfo.getDeclaringClass();
            String methodName = methodInfo.getMethodName();
            String descriptor = methodInfo.getDescriptor();
        }
}
