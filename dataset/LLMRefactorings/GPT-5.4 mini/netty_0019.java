public class netty_0019 {

        protected <A extends AbstractBootstrap<?, ?>, B extends AbstractBootstrap<?, ?>>
    
        List<BootstrapComboFactory<A, B>> comboUpdated(List<BootstrapFactory<A>> sbfs, List<BootstrapFactory<B>> cbfs) {
    
            List<BootstrapComboFactory<A, B>> list = new ArrayList<BootstrapComboFactory<A, B>>();
    
            // Populate the combinations
            for (BootstrapFactory<A> sbf: sbfs) {
                for (BootstrapFactory<B> cbf: cbfs) {
                    final BootstrapFactory<A> sbf0 = sbf;
                    final BootstrapFactory<B> cbf0 = cbf;
                    list.add(new BootstrapComboFactory<A, B>() {
                        @Override
                        public A newServerInstance() {
                            return sbf0.newInstance();
                        }
    
                        @Override
                        public B newClientInstance() {
                            return cbf0.newInstance();
                        }
                    });
                }
            }
    
            return list;
        }
}
