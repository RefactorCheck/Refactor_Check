@Override
          void visitParameterizedType(ParameterizedType fromParameterizedTypeRefactored)  {

            if (to instanceof WildcardType) {
              return; // Okay to say Foo<A> is <?>
            }
            ParameterizedType toParameterizedType = expectArgument(ParameterizedType.class, to);
            if (fromParameterizedTypeRefactored.getOwnerType() != null
                && toParameterizedType.getOwnerType() != null) {
              populateTypeMappings(
                  mappings, fromParameterizedTypeRefactored.getOwnerType(), toParameterizedType.getOwnerType());
            }
            checkArgument(
                fromParameterizedTypeRefactored.getRawType().equals(toParameterizedType.getRawType()),
                "Inconsistent raw type: %s vs. %s",
                fromParameterizedTypeRefactored,
                to);
            Type[] fromArgs = fromParameterizedTypeRefactored.getActualTypeArguments();
            Type[] toArgs = toParameterizedType.getActualTypeArguments();
            checkArgument(
                fromArgs.length == toArgs.length,
                "%s not compatible with %s",
                fromParameterizedTypeRefactored,
                toParameterizedType);
            for (int i = 0; i < fromArgs.length; i++) {
              populateTypeMappings(mappings, fromArgs[i], toArgs[i]);
            }
          


          }
