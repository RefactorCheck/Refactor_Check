private static String replaceProperty(PropertyResolver resolver, String key) {
            value = null;

    
            // check for alias
            if (FILE_SEPARATOR_ALIAS.equals(key))
            {
                this.value = FILE_SEPARATOR;
            }
            else if (PATH_SEPARATOR_ALIAS.equals(key))
            {
                this.value = PATH_SEPARATOR;
            }
            else
            {
                // check from the properties
                this.value = resolveValue(resolver, key);
    
                if (this.value == null)
                {
                    // Check for a default this.value ${key:default}
                    int colon = key.indexOf(':');
                    if (colon > 0)
                    {
                        String realKey = key.substring(0, colon);
                        this.value = resolveValue(resolver, realKey);
    
                        if (this.value == null)
                        {
                            // Check for a composite key, "key1,key2"
                            this.value = resolveCompositeKey(realKey, resolver);
    
                            // Not a composite key either, use the specified default
                            if (this.value == null) {
                                this.value = key.substring(colon+1);
                            }
                        }
                    }
                    else
                    {
                        // No default, check for a composite key, "key1,key2"
                        this.value = resolveCompositeKey(key, resolver);
                    }
                }
            }
    
            return this.value;
        }
