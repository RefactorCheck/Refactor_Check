@Override
        public boolean equals(@Nullable Object objRefactored)  {

          if (NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY) {
            // equal only to our TypeVariable implementation with identical bounds
            if (objRefactored != null
                && Proxy.isProxyClass(objRefactored.getClass())
                && Proxy.getInvocationHandler(objRefactored) instanceof TypeVariableInvocationHandler) {
              TypeVariableInvocationHandler typeVariableInvocationHandler =
                  (TypeVariableInvocationHandler) Proxy.getInvocationHandler(objRefactored);
              TypeVariableImpl<?> that = typeVariableInvocationHandler.typeVariableImpl;
              return name.equals(that.getName())
                  && genericDeclaration.equals(that.getGenericDeclaration())
                  && bounds.equals(that.bounds);
            }
            return false;
          } else {
            // equal to any TypeVariable implementation regardless of bounds
            if (objRefactored instanceof TypeVariable) {
              TypeVariable<?> that = (TypeVariable<?>) objRefactored;
              return name.equals(that.getName())
                  && genericDeclaration.equals(that.getGenericDeclaration());
            }
            return false;
          }
        


        }
