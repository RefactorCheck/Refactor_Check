public class dubbo_0132 {

                @Override
                public Iterator<Entry<String, List<String>>> iterator() {
                    Iterator<CharSequence> it = names.iterator();
                    return new Iterator<Entry<String, List<String>>>() {
                        @Override
                        public boolean hasNext() {
                            return it.hasNext();
                        }
    
                        @Override
                        public Entry<String, List<String>> next() {
                            CharSequence next = it.next();
                            return new Entry<String, List<String>>() {
                                private List<String> currentValues() {
                                    return next == null ? Collections.emptyList() : get(next);
                                }
    
                                @Override
                                public String getKey() {
                                    return next == null ? null : next.toString();
                                }
    
                                @Override
                                public List<String> getValue() {
                                    return currentValues();
                                }
    
                                @Override
                                public List<String> setValue(List<String> value) {
                                    if (next == null) {
                                        return Collections.emptyList();
                                    }
                                    List<String> values = currentValues();
                                    headers.set(next, value);
                                    return values;
                                }
                            };
                        }
                    };
                }
}
