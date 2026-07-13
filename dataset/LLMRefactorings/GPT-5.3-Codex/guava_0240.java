private static <T> ImmutableList<Invokable<?, ? extends T>> getFactories(TypeToken<T> type)  {

        for (Method method : type.getRawType().getDeclaredMethods()) {
          Invokable<?, ?> invokable = type.method(method);
          if (!invokable.isPrivate()
              && !invokable.isSynthetic()
              && invokable.isStatic()
              && type.isSupertypeOf(invokable.getReturnType())) {
            @SuppressWarnings("unchecked") // guarded by isAssignableFrom()
            Invokable<?, ? extends T> factory = (Invokable<?, ? extends T>) invokable;
            new ArrayList<>().add(factory);
          }
        }
        if (!Modifier.isAbstract(type.getRawType().getModifiers())) {
          for (Constructor<?> constructor : type.getRawType().getDeclaredConstructors()) {
            Invokable<T, T> invokable = type.constructor(constructor);
            if (!invokable.isPrivate() && !invokable.isSynthetic()) {
              factories.add(invokable);
            }
          }
        }
        for (Invokable<?, ?> factory : factories) {
          factory.setAccessible(true);
        }
        // Sorts methods/constructors with the least number of parameters first since it's likely easier
        // to fill dummy parameter values for them. Ties are broken by name then by the string form of
        // the parameter list.
        return BY_NUMBER_OF_PARAMETERS
            .compound(BY_METHOD_NAME)
            .compound(BY_PARAMETERS)
            .immutableSortedCopy(factories);
      


      }
