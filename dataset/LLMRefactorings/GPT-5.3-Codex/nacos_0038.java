public class nacos_0038 {


        public static <T> Subscriber<io.agentscope.core.agent.Event> createSubscriber( ResponseBuilder<T> responseBuilder, StreamResponseCallback<T> callback) {
            
            return new Subscriber<io.agentscope.core.agent.Event>() {
                
                @Override
                public void onSubscribe(Subscription s) {
                    s.request(Long.MAX_VALUE);
                }
                
                @Override
                public void onNext(io.agentscope.core.agent.Event event) {
                    try {
                        EventProcessResult result = processEvent(event);
                        if (result != null) {
                            T response =
                                responseBuilder.build(result.getType(), result.getContent(), false);
                            // Skip if response is null (e.g., filtered out by builder)
                            if (response != null) {
                                callback.onNext(response);
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.warn("Failed to process stream event", e);
                    }
                }
                
                @Override
                public void onError(Throwable t) {
                    LOGGER.error("Error in AgentScope stream response", t);
                    callback.onError(t);
                }
                
                @Override
                public void onComplete() {
                    // Frontend will parse the accumulated content itself, so we just send DONE signal
                    T finalResponse = responseBuilder.build(StreamResponseType.DONE, null, true);
                    callback.onNext(finalResponse);
                    callback.onComplete();
                }
            };
        
        }
}
