public class arthas_0166 {

        private ExitStatus processExactMatch(CommandProcess process, RowAffect affect, Instrumentation inst, Set<Class<?>> matchedClasses, Set<Class<?>> withInnerClasses) {
            Class<?> c = matchedClasses.iterator().next();
            Set<Class<?>> allClasses = new HashSet<>(withInnerClasses);
            allClasses.add(c);
            try {
                final ClassDumpTransformer transformer;
                if (directory == null) {
                    transformer = new ClassDumpTransformer(allClasses);
                } else {
                    transformer = new ClassDumpTransformer(allClasses, new File(directory));
                }
                InstrumentationUtils.retransformClasses(inst, transformer, allClasses);
    
                Map<Class<?>, File> classFiles = transformer.getDumpResult();
                if (classFiles == null || classFiles.isEmpty()) {
                    return ExitStatus.failure(-1, "jad: fail to dump class file for decompiler, make sure you have write permission of the directory \"" + transformer.dumpDir() +
                    "\" or try with \"-d/--directory\" to specify the directory of dump files");
                }
                File classFile = classFiles.get(c);
                Pair<String,NavigableMap<Integer,Integer>> decompileResult = Decompiler.decompileWithMappings(classFile.getAbsolutePath(), methodName, hideUnicode, lineNumber);
                String source = decompileResult.getFirst();
                JadModel jadModel = buildJadModel(c, source, decompileResult);
                process.appendResult(jadModel);
                affect.rCnt(classFiles.keySet().size());
                return ExitStatus.success();
            } catch (Throwable t) {
                logger.error("jad: fail to decompile class: " + c.getName(), t);
                return ExitStatus.failure(-1, "jad: fail to decompile class: " + c.getName() + ", please check $HOME/logs/arthas/arthas.log for more details.");
            }
        }

        private JadModel buildJadModel(Class<?> c, String source, Pair<String, NavigableMap<Integer, Integer>> decompileResult) {
            if (source != null) {
                source = pattern.matcher(source).replaceAll("");
            } else {
                source = "unknown";
            }
            JadModel jadModel = new JadModel();
            jadModel.setSource(source);
            jadModel.setMappings(decompileResult.getSecond());
            if (!this.sourceOnly) {
                jadModel.setClassInfo(ClassUtils.createSimpleClassInfo(c));
                jadModel.setLocation(ClassUtils.getCodeSource(c.getProtectionDomain().getCodeSource()));
            }
            return jadModel;
        }
}
