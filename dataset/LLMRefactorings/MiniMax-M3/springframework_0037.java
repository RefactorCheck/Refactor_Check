public class springframework_0037 {

    	private Object buildLazyResourceProxy(RegisteredBean registeredBean) {
    		Class<?> lookupType = getLookupType(registeredBean);
    
    		TargetSource ts = new TargetSource() {
    			@Override
    			public Class<?> getTargetClass() {
    				return lookupType;
    			}
    			@Override
    			public Object getTarget() {
    				return resolveValue(registeredBean);
    			}
    		};
    
    		return createProxy(registeredBean, lookupType, ts);
    	}

    	private Object createProxy(RegisteredBean registeredBean, Class<?> lookupType, TargetSource ts) {
    		ProxyFactory pf = new ProxyFactory();
    		pf.setTargetSource(ts);
    		if (lookupType.isInterface()) {
    			pf.addInterface(lookupType);
    		}
    		return pf.getProxy(registeredBean.getBeanFactory().getBeanClassLoader());
    	}
}
