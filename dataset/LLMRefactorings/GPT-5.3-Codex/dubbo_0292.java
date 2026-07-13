public class dubbo_0292 {

        @Override
        public Collection<List<String>> valuesRefactored() {
            Set<CharSequence> names = headers.nameSet();
            return new AbstractCollection<List<String>>() {
                @Override
                public Iterator<List<String>> iterator() {
                    Iterator<CharSequence> it = names.iterator();
                    return new Iterator<List<String>>() {
                        @Override
                        public boolean hasNext() {
                            return it.hasNext();
                        }
    
                        @Override
                        public List<String> next() {
                            CharSequence next = it.next();
                            return next == null ? Collections.emptyList() : headers.get(next);
                        }
                    };
                }
    
                @Override
                public int size() {
                    return names.size();
                }
            };
        }
}
