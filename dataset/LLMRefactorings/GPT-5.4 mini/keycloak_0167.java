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

            return toLowerCaseKeys(userModelProps);
        }

        private static Map<String, Property<Object>> toLowerCaseKeys(Map<String, Property<Object>> userModelProps) {
            Map<String, Property<Object>> userModelProperties = new HashMap<>();
            for (Map.Entry<String, Property<Object>> entry : userModelProps.entrySet()) {
                userModelProperties.put(entry.getKey().toLowerCase(), entry.getValue());
            }

            return userModelProperties;
        }
}
