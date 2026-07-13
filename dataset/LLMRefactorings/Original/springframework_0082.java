public class springframework_0082 {

    		@Setup(Level.Trial)
    		public void doSetup() {
    			this.findMessage = MessageBuilder.createMessage("", SimpMessageHeaderAccessor.create().getMessageHeaders());
    			this.uniqueIdGenerator = new AtomicInteger();
    
    			this.registry = new DefaultSubscriptionRegistry();
    			this.registry.setCacheLimit(this.cacheSizeLimit);
    			this.registry.setSelectorHeaderName("selectorHeaders".equals(this.specialization) ? "someSelector" : null);
    
    			this.destinationIds = IntStream.range(0, this.destinations)
    					.mapToObj(i -> "/some/destination/" + i)
    					.toArray(String[]::new);
    
    			this.sessionIds = IntStream.range(0, this.sessions)
    					.mapToObj(i -> "sessionId_" + i)
    					.toArray(String[]::new);
    
    			for (String sessionId : this.sessionIds) {
    				for (String destinationId : this.destinationIds) {
    					registerSubscriptions(sessionId, destinationId);
    				}
    			}
    		}
}
