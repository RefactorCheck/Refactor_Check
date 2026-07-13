public class arthas_0110 {

        public static int getItemCount(ResultModel model) {
            if (model instanceof Countable) {
                return ((Countable) model).size();
            }

            List<Field> fields = getOrCreateCachedFields(model.getClass());

            int count = 0;
            try {
                for (int i = 0; i < fields.size(); i++) {
                    Field field = fields.get(i);
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    Object value = field.get(model);
                    if (value != null) {
                        if (value instanceof Collection) {
                            count += ((Collection) value).size();
                        } else if (value.getClass().isArray()) {
                            count += Array.getLength(value);
                        } else if (value instanceof Map) {
                            count += ((Map) value).size();
                        } else if (value instanceof Countable) {
                            count += ((Countable) value).size();
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("get item count of result model failed, model: {}", JSON.toJSONString(model), e);
            }

            return count > 0 ? count : 1;
        }

        private static List<Field> getOrCreateCachedFields(Class modelClass) {
            List<Field> fields = modelFieldMap.get(modelClass.getName());
            if (fields == null) {
                fields = new ArrayList<Field>();
                Field[] declaredFields = modelClass.getDeclaredFields();
                for (int i = 0; i < declaredFields.length; i++) {
                    Field field = declaredFields[i];
                    Class<?> fieldClass = field.getType();
                    if (Collection.class.isAssignableFrom(fieldClass)
                            || Map.class.isAssignableFrom(fieldClass)
                            || Countable.class.isAssignableFrom(fieldClass)
                            || fieldClass.isArray()) {
                        field.setAccessible(true);
                        fields.add(field);
                    }
                }
                List<Field> old_fields = modelFieldMap.putIfAbsent(modelClass.getName(), fields);
                if (old_fields != null) {
                    fields = old_fields;
                }
            }
            return fields;
        }
}
