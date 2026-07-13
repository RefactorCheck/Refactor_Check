public class keycloak_0167 {

        public static Map<String, Property<Object>> getUserModelProperties(){

            Map<String, Property<Object>> userModelProps = PropertyQueries.createQuery(UserModel.class)
                    .addCriteria(new PropertyCriteria() {

                        @Override
                        public boolean methodMatches(Method m) {
                            if ((m.getName().startsWith("get") || m.getName().startsWith("is"))
                                    && m.getParameterCount() > 0) {
                                return false;
                            }

                            return true;
                        }

                    }).getResultList();

            return convertToLowerCaseKeys(userModelProps);
        }

        private static Map<String, Property<Object>> convertToLowerCaseKeys(Map<String, Property<Object>> properties) {
            Map<String, Property<Object>> result = new HashMap<>();
            for (Map.Entry<String, Property<Object>> entry : properties.entrySet()) {
                result.put(entry.getKey().toLowerCase(), entry.getValue());
            }
            return result;
        }
}
