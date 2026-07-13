public class keycloak_0189 {

        private static String replaceProperty(PropertyResolver resolver, String key) {
            String value = null;
    
            // check for alias
            if (FILE_SEPARATOR_ALIAS.equals(key))
            {
                value = FILE_SEPARATOR;
            }
            else if (PATH_SEPARATOR_ALIAS.equals(key))
            {
                value = PATH_SEPARATOR;
            }
            else
            {
                // check from the properties
                value = resolveValue(resolver, key);
    
                if (value == null)
                {
                    // Check for a default value ${key:default}
                    int colon = key.indexOf(':');
                    if (colon > 0)
                    {
                        String realKey = key.substring(0, colon);
                        value = resolveValue(resolver, realKey);
    
                        if (value == null)
                        {
                            // Check for a composite key, "key1,key2"
                            value = resolveCompositeKey(realKey, resolver);
    
                            // Not a composite key either, use the specified default
                            if (value == null) {
                                value = key.substring(colon+1);
                            }
                        }
                    }
                    else
                    {
                        // No default, check for a composite key, "key1,key2"
                        value = resolveCompositeKey(key, resolver);
                    }
                }
            }
    
            return value;
        }
}
