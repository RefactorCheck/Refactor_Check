public class keycloak_0189 {

        private static String replaceProperty(PropertyResolver resolver, String key) {
            String resolvedValue = null;

            if (FILE_SEPARATOR_ALIAS.equals(key))
            {
                resolvedValue = FILE_SEPARATOR;
            }
            else if (PATH_SEPARATOR_ALIAS.equals(key))
            {
                resolvedValue = PATH_SEPARATOR;
            }
            else
            {
                resolvedValue = resolveValue(resolver, key);

                if (resolvedValue == null)
                {
                    int colon = key.indexOf(':');
                    if (colon > 0)
                    {
                        String realKey = key.substring(0, colon);
                        resolvedValue = resolveValue(resolver, realKey);

                        if (resolvedValue == null)
                        {
                            resolvedValue = resolveCompositeKey(realKey, resolver);

                            if (resolvedValue == null) {
                                resolvedValue = key.substring(colon+1);
                            }
                        }
                    }
                    else
                    {
                        resolvedValue = resolveCompositeKey(key, resolver);
                    }
                }
            }

            return resolvedValue;
        }
}
