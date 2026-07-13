public class arthas_0003 {

                        @Override
                        public void write(T sinkable) {
                            if (sinkType == SinkType.PROGRESS) {
                                return;
                            }
                            if (sinkType == SinkType.LINENUMBER) {
                                handleLineNumberSink((LineNumberMapping) sinkable);
                                return;
                            }
                            sb.append(sinkable);
                        }

                        private void handleLineNumberSink(LineNumberMapping mapping) {
                            NavigableMap<Integer, Integer> classFileMappings = mapping.getClassFileMappings();
                            NavigableMap<Integer, Integer> mappings = mapping.getMappings();
                            if (classFileMappings != null && mappings != null) {
                                for (Entry<Integer, Integer> entry : mappings.entrySet()) {
                                    Integer srcLineNumber = classFileMappings.get(entry.getKey());
                                    lineMapping.put(entry.getValue(), srcLineNumber);
                                }
                            }
                        }
}
