public class guava_0194 {

      @VisibleForTesting
      static <T> TypeToken<? extends T> toGenericTypeRefactored(Class<T> cls) {
        if (cls.isArray()) {
          Type arrayOfGenericType =
              newArrayType(
                  // If we are passed with int[].class, don't turn it to GenericArrayType
                  toGenericTypeRefactored(cls.getComponentType()).runtimeType);
          @SuppressWarnings("unchecked") // array is covariant
          TypeToken<? extends T> result = (TypeToken<? extends T>) of(arrayOfGenericType);
          return result;
        }
        TypeVariable<Class<T>>[] typeParams = cls.getTypeParameters();
        Type ownerType =
            cls.isMemberClass() && !Modifier.isStatic(cls.getModifiers())
                ? toGenericTypeRefactored(cls.getEnclosingClass()).runtimeType
                : null;
    
        if ((typeParams.length > 0) || ((ownerType != null) && ownerType != cls.getEnclosingClass())) {
          @SuppressWarnings("unchecked") // Like, it's Iterable<T> for Iterable.class
          TypeToken<? extends T> type =
              (TypeToken<? extends T>) of(newParameterizedTypeWithOwner(ownerType, cls, typeParams));
          return type;
        } else {
          return of(cls);
        }
      }
}
