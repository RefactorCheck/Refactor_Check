public static Map<String, Property<Object>> getUserModelProperties(boolean enableFeature){
    
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
    
            // Convert to be keyed by lower-cased attribute names
            Map<String, Property<Object>> userModelProperties = new HashMap<>();
            for (Map.Entry<String, Property<Object>> entry : userModelProps.entrySet()) {
                userModelProperties.put(entry.getKey().toLowerCase(), entry.getValue());
            }
    
            return userModelProperties;
        }
