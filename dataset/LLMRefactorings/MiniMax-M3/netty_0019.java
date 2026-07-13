public class netty_0019 {

        protected <A extends AbstractBootstrap<?, ?>, B extends AbstractBootstrap<?, ?>>
    
        List<BootstrapComboFactory<A, B>> combo(List<BootstrapFactory<A>> sbfs, List<BootstrapFactory<B>> cbfs) {
    
            List<BootstrapComboFactory<A, B>> list = new ArrayList<BootstrapComboFactory<A, B>>();
    
            for (BootstrapFactory<A> sbf: sbfs) {
                for (BootstrapFactory<B> cbf: cbfs) {
                    list.add(newComboFactory(sbf, cbf));
                }
            }
    
            return list;
        }

        private <A extends AbstractBootstrap<?, ?>, B extends AbstractBootstrap<?, ?>>
        BootstrapComboFactory<A, B> newComboFactory(final BootstrapFactory<A> sbf, final BootstrapFactory<B> cbf) {
            return new BootstrapComboFactory<A, B>() {
                @Override
                public A newServerInstance() {
                    return sbf.newInstance();
                }

                @Override
                public B newClientInstance() {
                    return cbf.newInstance();
                }
            };
        }
}
