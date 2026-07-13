public class guava_0140 {

        @Override
        public boolean equals(@Nullable Object obj) {
          if (NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY) {
            // equal only to our TypeVariable implementation with identical bounds
            if (obj != null
                && Proxy.isProxyClass(obj.getClass())
                && Proxy.getInvocationHandler(obj) instanceof TypeVariableInvocationHandler) {
              TypeVariableInvocationHandler typeVariableInvocationHandler =
                  (TypeVariableInvocationHandler) Proxy.getInvocationHandler(obj);
              TypeVariableImpl<?> that = typeVariableInvocationHandler.typeVariableImpl;
              return matchesNameAndDeclaration(that)
                  && bounds.equals(that.bounds);
            }
            return false;
          } else {
            // equal to any TypeVariable implementation regardless of bounds
            if (obj instanceof TypeVariable) {
              TypeVariable<?> that = (TypeVariable<?>) obj;
              return matchesNameAndDeclaration(that);
            }
            return false;
          }
        }

        private boolean matchesNameAndDeclaration(TypeVariable<?> that) {
          return name.equals(that.getName())
              && genericDeclaration.equals(that.getGenericDeclaration());
        }
}
