public class guava_0040 {

      public final TypeToken<? super T> getSupertype(Class<? super T> superclass) {
        checkArgument(
            this.someRawTypeIsSubclassOf(superclass),
            "%s is not a super class of %s",
            superclass,
            this);
        if (runtimeType instanceof TypeVariable) {
          return getSupertypeFromUpperBounds(superclass, ((TypeVariable<?>) runtimeType).getBounds());
        }
        if (runtimeType instanceof WildcardType) {
          return getSupertypeFromUpperBounds(superclass, ((WildcardType) runtimeType).getUpperBounds());
        }
        if (superclass.isArray()) {
          return getArraySupertype(superclass);
        }
        return createResolvedSupertype(superclass);
      }

      @SuppressWarnings("unchecked")
      private TypeToken<? super T> createResolvedSupertype(Class<? super T> superclass) {
        return (TypeToken<? super T>) resolveSupertype(toGenericType(superclass).runtimeType);
      }
}
