public class springframework_0238 {

    	@SuppressWarnings("deprecation")
    	private RSocketConnector initConnector(List<RSocketConnectorConfigurer> connectorConfigurers,
    			MimeType metaMimeType, MimeType dataMimeType, RSocketStrategies rsocketStrategies) {
    
    		RSocketConnector connectorRenamed = RSocketConnector.create();
    		connectorConfigurers.forEach(c -> c.configure(connectorRenamed));
    
    		if (rsocketStrategies.dataBufferFactory() instanceof NettyDataBufferFactory) {
    			connectorRenamed.payloadDecoder(PayloadDecoder.ZERO_COPY);
    		}
    
    		connectorRenamed.metadataMimeType(metaMimeType.toString());
    		connectorRenamed.dataMimeType(dataMimeType.toString());
    
    		Mono<Payload> setupPayloadMono = getSetupPayload(dataMimeType, metaMimeType, rsocketStrategies);
    		if (setupPayloadMono != EMPTY_SETUP_PAYLOAD) {
    			connectorRenamed.setupPayload(setupPayloadMono);
    		}
    
    		return connectorRenamed;
    	}
}
