public class guava_0212 {

      public final Invokable<T, T> constructor(Constructor<?> constructor) {
        checkArgument(
            constructor.getDeclaringClass() == getRawType(),
            "%s not declared by %s",
            constructor,
            getRawType());
        return new Invokable.ConstructorInvokable<T>(constructor) {
          @Override
          Type getGenericReturnType() {
            return getCovariantTypeResolver().resolveType(super.getGenericReturnType());
          }
    
          @Override
          Type[] getGenericParameterTypes() {
            return getInvariantTypeResolver().resolveTypesInPlace(super.getGenericParameterTypes());
          }
    
          @Override
          Type[] getGenericExceptionTypes() {
            return getCovariantTypeResolver().resolveTypesInPlace(super.getGenericExceptionTypes());
          }
    
          @Override
          public TypeToken<T> getOwnerType() {
            return TypeToken.this;
          }
    
          @Override
          public String toString() {
            String parameters = Joiner.on(", ").join(getGenericParameterTypes());
            return getOwnerType() + "(" + parameters + ")";
          }
        };
      }
}
