public class arthas_0087 {

        @Override
        public void draw(CommandProcess process, SearchMethodModel result, final boolean useCache) {
            boolean cacheEnabled = useCache;
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
                    //render constructor
                    process.write(RenderUtil.render(ClassUtils.renderConstructor(methodInfo), process.width()) + "\n");
                } else {
                    //render method
                    process.write(RenderUtil.render(ClassUtils.renderMethod(methodInfo), process.width()) + "\n");
                }
            } else {
                //java.util.List indexOf(Ljava/lang/Object;)I
                //className methodName+Descriptor
                process.write(methodInfo.getDeclaringClass())
                        .write(" ")
                        .write(methodInfo.getMethodName())
                        .write(methodInfo.getDescriptor())
                        .write("\n");
            }
    
        }
}
