public class guava_0240 {

      private static <T> ImmutableList<Invokable<?, ? extends T>> getFactories(TypeToken<T> type) {
        List<Invokable<?, ? extends T>> factories = new ArrayList<>();
        collectStaticFactoryMethods(type, factories);
        if (!Modifier.isAbstract(type.getRawType().getModifiers())) {
          collectFactoryConstructors(type, factories);
        }
        for (Invokable<?, ?> factory : factories) {
          factory.setAccessible(true);
        }
        return BY_NUMBER_OF_PARAMETERS
            .compound(BY_METHOD_NAME)
            .compound(BY_PARAMETERS)
            .immutableSortedCopy(factories);
      }

      private static <T> void collectStaticFactoryMethods(TypeToken<T> type, List<Invokable<?, ? extends T>> factories) {
        for (Method method : type.getRawType().getDeclaredMethods()) {
          Invokable<?, ?> invokable = type.method(method);
          if (!invokable.isPrivate()
              && !invokable.isSynthetic()
              && invokable.isStatic()
              && type.isSupertypeOf(invokable.getReturnType())) {
            @SuppressWarnings("unchecked") // guarded by isAssignableFrom()
            Invokable<?, ? extends T> factory = (Invokable<?, ? extends T>) invokable;
            factories.add(factory);
          }
        }
      }

      private static <T> void collectFactoryConstructors(TypeToken<T> type, List<Invokable<?, ? extends T>> factories) {
        for (Constructor<?> constructor : type.getRawType().getDeclaredConstructors()) {
          Invokable<T, T> invokable = type.constructor(constructor);
          if (!invokable.isPrivate() && !invokable.isSynthetic()) {
            factories.add(invokable);
          }
        }
      }
}
