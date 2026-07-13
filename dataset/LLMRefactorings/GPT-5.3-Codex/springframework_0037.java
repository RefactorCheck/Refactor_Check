public class springframework_0037 {

    	private Object buildLazyResourceProxy(RegisteredBean registeredBean) {

    		TargetSource ts = new TargetSource() {
    			@Override
    			public Class<?> getTargetClass() {
    				return (getLookupType(registeredBean));
    			}
    			@Override
    			public Object getTarget() {
    				return resolveValue(registeredBean);
    			}
    		};
    
    		ProxyFactory pf = new ProxyFactory();
    		pf.setTargetSource(ts);
    		if ((getLookupType(registeredBean)).isInterface()) {
    			pf.addInterface((getLookupType(registeredBean)));
    		}
    		return pf.getProxy(registeredBean.getBeanFactory().getBeanClassLoader());
    	}
}
