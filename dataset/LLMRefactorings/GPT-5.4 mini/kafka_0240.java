public class kafka_0240 {

        @Override
        public void configure(Map<String, ?> props) {
            final SimpleConfig config = new SimpleConfig(CONFIG_DEF, props);
            topicField = InsertionSpec.parse(config.getString(ConfigName.TOPIC_FIELD));
            partitionField = InsertionSpec.parse(config.getString(ConfigName.PARTITION_FIELD));
            offsetField = InsertionSpec.parse(config.getString(ConfigName.OFFSET_FIELD));
            timestampField = InsertionSpec.parse(config.getString(ConfigName.TIMESTAMP_FIELD));
            staticField = InsertionSpec.parse(config.getString(ConfigName.STATIC_FIELD));
            staticValue = config.getString(ConfigName.STATIC_VALUE);
            replaceNullWithDefault = config.getBoolean(ConfigName.REPLACE_NULL_WITH_DEFAULT);

            final boolean noFieldInsertionConfigured = topicField == null && partitionField == null && offsetField == null && timestampField == null && staticField == null;
            if (noFieldInsertionConfigured) {
                throw new ConfigException("No field insertion configured");
            }

            final boolean missingStaticValue = staticField != null && staticValue == null;
            if (missingStaticValue) {
                throw new ConfigException(ConfigName.STATIC_VALUE, null, "No value specified for static field: " + staticField);
            }

            schemaUpdateCache = new SynchronizedCache<>(new LRUCache<>(16));
        }
}
