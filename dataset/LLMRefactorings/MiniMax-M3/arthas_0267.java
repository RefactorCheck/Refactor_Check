public class arthas_0267 {

        public Map<String, byte[]> buildByteCodes() {
    
            errors.clear();
            warnings.clear();
    
            JavaFileManager fileManager = new DynamicJavaFileManager(standardFileManager, dynamicClassLoader);
    
            DiagnosticCollector<JavaFileObject> collector = new DiagnosticCollector<JavaFileObject>();
            JavaCompiler.CompilationTask task = javaCompiler.getTask(null, fileManager, collector, options, null,
                            compilationUnits);
    
            try {
    
                if (!compilationUnits.isEmpty()) {
                    boolean result = task.call();
    
                    if (!result || collector.getDiagnostics().size() > 0) {
                        processDiagnostics(collector.getDiagnostics());
    
                        if (!errors.isEmpty()) {
                            throw new DynamicCompilerException("Compilation Error", errors);
                        }
                    }
                }
    
                return dynamicClassLoader.getByteCodes();
            } catch (ClassFormatError e) {
                throw new DynamicCompilerException(e, errors);
            } finally {
                compilationUnits.clear();
    
            }
    
        }

        private void processDiagnostics(java.util.List<Diagnostic<? extends JavaFileObject>> diagnostics) {
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
                switch (diagnostic.getKind()) {
                case NOTE:
                case MANDATORY_WARNING:
                case WARNING:
                    warnings.add(diagnostic);
                    break;
                case OTHER:
                case ERROR:
                default:
                    errors.add(diagnostic);
                    break;
                }
            }
        }
}
