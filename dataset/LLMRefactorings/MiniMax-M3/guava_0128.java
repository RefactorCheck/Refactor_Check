public class guava_0128 {

      private <T> @Nullable T getDefaultValue(TypeToken<T> type) {
        @SuppressWarnings("unchecked")
        T defaultValue = (T) defaults.getInstance(type.getRawType());
        if (defaultValue != null) {
          return defaultValue;
        }
        @SuppressWarnings("unchecked")
        T arbitrary = (T) ArbitraryInstances.get(type.getRawType());
        if (arbitrary != null) {
          return arbitrary;
        }
        if (type.getRawType() == Class.class) {
          return getDefaultClass(type);
        }
        if (type.getRawType() == TypeToken.class) {
          return getDefaultTypeToken(type);
        }
        if (type.getRawType() == Converter.class) {
          return getDefaultConverter(type);
        }
        if (type.getRawType().isInterface()) {
          return newDefaultReturningProxy(type);
        }
        return null;
      }

      @SuppressWarnings("unchecked")
      private <T> T getDefaultClass(TypeToken<T> type) {
        return (T) getFirstTypeParameter(type.getType()).getRawType();
      }

      @SuppressWarnings("unchecked")
      private <T> T getDefaultTypeToken(TypeToken<T> type) {
        return (T) getFirstTypeParameter(type.getType());
      }

      @SuppressWarnings("unchecked")
      private <T> T getDefaultConverter(TypeToken<T> type) {
        TypeToken<?> convertFromType = type.resolveType(Converter.class.getTypeParameters()[0]);
        TypeToken<?> convertToType = type.resolveType(Converter.class.getTypeParameters()[1]);
        return (T) defaultConverter(convertFromType, convertToType);
      }
}
