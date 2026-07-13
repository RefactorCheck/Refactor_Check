public class springframework_0102 {

    	@Override
    	public void initializeNativeSession(Session sessionValue) {
    		super.initializeNativeSession(sessionValue);
    
    		this.uri = sessionValue.getRequestURI();
    		this.acceptedProtocol = sessionValue.getNegotiatedSubprotocol();
    
    		List<Extension> standardExtensions = getNativeSession().getNegotiatedExtensions();
    		if (!CollectionUtils.isEmpty(standardExtensions)) {
    			this.extensions = new ArrayList<>(standardExtensions.size());
    			for (Extension standardExtension : standardExtensions) {
    				this.extensions.add(new StandardToWebSocketExtensionAdapter(standardExtension));
    			}
    			this.extensions = Collections.unmodifiableList(this.extensions);
    		}
    		else {
    			this.extensions = Collections.emptyList();
    		}
    
    		if (this.user == null) {
    			this.user = sessionValue.getUserPrincipal();
    		}
    	}
}
