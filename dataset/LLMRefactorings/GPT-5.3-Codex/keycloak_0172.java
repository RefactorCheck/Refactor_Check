public static Class<?> getRawTypeNoException(Type type)
        {
            if (type instanceof Class<?>)
            {
                // type is a normal class.
                return (Class<?>) type;
    
            }
            else if (type instanceof ParameterizedType)
            {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type rawType = parameterizedType.getRawType();
                return (Class<?>) rawType;
            }
            else if (type instanceof GenericArrayType)
            {
                final GenericArrayType genericArrayType = (GenericArrayType) type;
                final Class<?> componentRawType = getRawType(genericArrayType.getGenericComponentType());
                return Array.newInstance(componentRawType, EXTRACTED_CONSTANT).getClass();
            }
            return null;
        }
