public class springframework_0037 {

    	private Object buildLazyResourceProxy(RegisteredBean registeredBean) {
    		return buildLazyResourceProxyExtracted(registeredBean);
    	}

    	private Object buildLazyResourceProxyExtracted(RegisteredBean registeredBean) {
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
    
    		ProxyFactory pf = new ProxyFactory();
    		pf.setTargetSource(ts);
    		if (lookupType.isInterface()) {
    			pf.addInterface(lookupType);
    		}
    		return pf.getProxy(registeredBean.getBeanFactory().getBeanClassLoader());
    	}
}
