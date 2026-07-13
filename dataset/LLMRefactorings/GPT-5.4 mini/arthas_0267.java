public class arthas_0267 {
    private static final String REFACTORED_CONSTANT = "Compilation Error";


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
    
                        for (Diagnostic<? extends JavaFileObject> diagnostic : collector.getDiagnostics()) {
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
    
                        if (!errors.isEmpty()) {
                            throw new DynamicCompilerException(REFACTORED_CONSTANT, errors);
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
}
