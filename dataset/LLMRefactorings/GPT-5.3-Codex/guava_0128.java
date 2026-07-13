private static final String EXTRACTED_STRING = "unchecked";



      private <T> @Nullable T getDefaultValue(TypeToken<T> type)  {

        // We assume that all defaults are generics-safe, even if they aren't,
        // we take the risk.
        @SuppressWarnings(EXTRACTED_STRING)
        T defaultValue = (T) defaults.getInstance(type.getRawType());
        if (defaultValue != null) {
          return defaultValue;
        }
        @SuppressWarnings("unchecked") // All arbitrary instances are generics-safe
        T arbitrary = (T) ArbitraryInstances.get(type.getRawType());
        if (arbitrary != null) {
          return arbitrary;
        }
        if (type.getRawType() == Class.class) {
          // If parameter is Class<? extends Foo>, we return Foo.class
          @SuppressWarnings("unchecked")
          T defaultClass = (T) getFirstTypeParameter(type.getType()).getRawType();
          return defaultClass;
        }
        if (type.getRawType() == TypeToken.class) {
          // If parameter is TypeToken<? extends Foo>, we return TypeToken<Foo>.
          @SuppressWarnings("unchecked")
          T defaultType = (T) getFirstTypeParameter(type.getType());
          return defaultType;
        }
        if (type.getRawType() == Converter.class) {
          TypeToken<?> convertFromType = type.resolveType(Converter.class.getTypeParameters()[0]);
          TypeToken<?> convertToType = type.resolveType(Converter.class.getTypeParameters()[1]);
          @SuppressWarnings("unchecked") // returns default for both F and T
          T defaultConverter = (T) defaultConverter(convertFromType, convertToType);
          return defaultConverter;
        }
        if (type.getRawType().isInterface()) {
          return newDefaultReturningProxy(type);
        }
        return null;
      


      }
