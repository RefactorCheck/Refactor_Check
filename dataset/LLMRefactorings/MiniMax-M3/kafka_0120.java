public class kafka_0120 {

        public void complete(KafkaFutureImpl<Map<ClientQuotaEntity, Map<String, Double>>> future) {
            Errors error = Errors.forCode(data.errorCode());
            if (error != Errors.NONE) {
                future.completeExceptionally(error.exception(data.errorMessage()));
                return;
            }
    
            Map<ClientQuotaEntity, Map<String, Double>> result = new HashMap<>(data.entries().size());
            for (EntryData entryData : data.entries()) {
                result.put(new ClientQuotaEntity(extractEntity(entryData)), extractValues(entryData));
            }
            future.complete(result);
        }

        private Map<String, String> extractEntity(EntryData entryData) {
            Map<String, String> entity = new HashMap<>(entryData.entity().size());
            for (EntityData entityData : entryData.entity()) {
                entity.put(entityData.entityType(), entityData.entityName());
            }
            return entity;
        }

        private Map<String, Double> extractValues(EntryData entryData) {
            Map<String, Double> values = new HashMap<>(entryData.values().size());
            for (ValueData valueData : entryData.values()) {
                values.put(valueData.key(), valueData.value());
            }
            return values;
        }
}
