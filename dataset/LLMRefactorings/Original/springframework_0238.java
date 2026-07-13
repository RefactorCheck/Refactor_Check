public class springframework_0238 {

    	@SuppressWarnings("deprecation")
    	private RSocketConnector initConnector(List<RSocketConnectorConfigurer> connectorConfigurers,
    			MimeType metaMimeType, MimeType dataMimeType, RSocketStrategies rsocketStrategies) {
    
    		RSocketConnector connector = RSocketConnector.create();
    		connectorConfigurers.forEach(c -> c.configure(connector));
    
    		if (rsocketStrategies.dataBufferFactory() instanceof NettyDataBufferFactory) {
    			connector.payloadDecoder(PayloadDecoder.ZERO_COPY);
    		}
    
    		connector.metadataMimeType(metaMimeType.toString());
    		connector.dataMimeType(dataMimeType.toString());
    
    		Mono<Payload> setupPayloadMono = getSetupPayload(dataMimeType, metaMimeType, rsocketStrategies);
    		if (setupPayloadMono != EMPTY_SETUP_PAYLOAD) {
    			connector.setupPayload(setupPayloadMono);
    		}
    
    		return connector;
    	}
}
