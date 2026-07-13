public class guava_0222 {

      static @Nullable Type getComponentType(Type type) {
        checkNotNull(type);
        AtomicReference<@Nullable Type> componentType = new AtomicReference<>();
        new TypeVisitor() {
          @Override
          void visitTypeVariable(TypeVariable<?> t) {
            componentType.set(subtypeOfComponentType(t.getBounds()));
          }
    
          @Override
          void visitWildcardType(WildcardType t) {
            componentType.set(subtypeOfComponentType(t.getUpperBounds()));
          }
    
          @Override
          void visitGenericArrayType(GenericArrayType t) {
            componentType.set(t.getGenericComponentType());
          }
    
          @Override
          void visitClass(Class<?> t) {
            componentType.set(t.getComponentType());
          }
        }.visit(type);
        return componentType.get();
      }
}
