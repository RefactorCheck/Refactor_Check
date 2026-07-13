public class guava_0222 {

      static @Nullable Type getComponentType(Type type) {
        checkNotNull(type);
        AtomicReference<@Nullable Type> result = new AtomicReference<>();
        new TypeVisitor() {
          @Override
          void visitTypeVariable(TypeVariable<?> t) {
            result.set(subtypeOfComponentType(t.getBounds()));
          }
    
          @Override
          void visitWildcardType(WildcardType t) {
            result.set(subtypeOfComponentType(t.getUpperBounds()));
          }
    
          @Override
          void visitGenericArrayType(GenericArrayType t) {
            result.set(t.getGenericComponentType());
          }
    
          @Override
          void visitClass(Class<?> t) {
            result.set(t.getComponentType());
          }
        }.visit(type);
        return result.get();
      }
}
