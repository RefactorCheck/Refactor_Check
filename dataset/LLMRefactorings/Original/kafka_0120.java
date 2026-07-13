public class kafka_0120 {

        public void complete(KafkaFutureImpl<Map<ClientQuotaEntity, Map<String, Double>>> future) {
            Errors error = Errors.forCode(data.errorCode());
            if (error != Errors.NONE) {
                future.completeExceptionally(error.exception(data.errorMessage()));
                return;
            }
    
            Map<ClientQuotaEntity, Map<String, Double>> result = new HashMap<>(data.entries().size());
            for (EntryData entries : data.entries()) {
                Map<String, String> entity = new HashMap<>(entries.entity().size());
                for (EntityData entityData : entries.entity()) {
                    entity.put(entityData.entityType(), entityData.entityName());
                }
    
                Map<String, Double> values = new HashMap<>(entries.values().size());
                for (ValueData valueData : entries.values()) {
                    values.put(valueData.key(), valueData.value());
                }
    
                result.put(new ClientQuotaEntity(entity), values);
            }
            future.complete(result);
        }
}
