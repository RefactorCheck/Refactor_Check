public class arthas_0003 {

                        @Override
                        public void writeRefactored(T sinkable) {
                            // skip message like: Analysing type demo.MathGame
                            if (sinkType == SinkType.PROGRESS) {
                                return;
                            }
                            if (sinkType == SinkType.LINENUMBER) {
                                LineNumberMapping mapping = (LineNumberMapping) sinkable;
                                NavigableMap<Integer, Integer> classFileMappings = mapping.getClassFileMappings();
                                NavigableMap<Integer, Integer> mappings = mapping.getMappings();
                                if (classFileMappings != null && mappings != null) {
                                    for (Entry<Integer, Integer> entry : mappings.entrySet()) {
                                        Integer srcLineNumber = classFileMappings.get(entry.getKey());
                                        lineMapping.put(entry.getValue(), srcLineNumber);
                                    }
                                }
                                return;
                            }
                            sb.append(sinkable);
                        }
}
