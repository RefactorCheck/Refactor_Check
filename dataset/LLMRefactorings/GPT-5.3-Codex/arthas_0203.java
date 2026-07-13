public class arthas_0203 {

                @Override
                public <T> Sink<T> getSink(final SinkType sinkType, final SinkClass sinkClass) {
                    return new Sink<T>() {
                        @Override
                        public void write(T sinkable) {
                            // skip message like: Analysing type demo.MathGame
                            if (sinkType == SinkType.PROGRESS) {
                                return;
                            }
                            if (sinkType == SinkType.LINENUMBER) {
                                refactorExtractedMethod();
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
                    };
                }

                private void refactorExtractedMethod() {
                                LineNumberMapping mapping = (LineNumberMapping) sinkable;
                }
}
