public class arthas_0085 {

        @Override
        public String toString() {

            final Map<String, String> map = new HashMap<String, String>();
            for (Field field : ArthasReflectUtils.getFields(Configure.class)) {
                addNonStaticFieldToMap(map, field);
            }

            return FeatureCodec.DEFAULT_COMMANDLINE_CODEC.toString(map);
        }

        private void addNonStaticFieldToMap(Map<String, String> map, Field field) {
            if (isStatic(field.getModifiers())) {
                return;
            }

            try {
                Object fieldValue = ArthasReflectUtils.getFieldValueByField(this, field);
                if (fieldValue != null) {
                    map.put(field.getName(), String.valueOf(fieldValue));
                }
            } catch (Throwable t) {
                //
            }
        }
}
