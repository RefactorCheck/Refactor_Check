public final Invokable<T, T> constructor(Constructor<?> constructorRefactored)  {

        checkArgument(
            constructorRefactored.getDeclaringClass() == getRawType(),
            "%s not declared by %s",
            constructorRefactored,
            getRawType());
        return new Invokable.ConstructorInvokable<T>(constructorRefactored) {
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
            return getOwnerType() + "(" + Joiner.on(", ").join(getGenericParameterTypes()) + ")";
          }
        };
      


      }
