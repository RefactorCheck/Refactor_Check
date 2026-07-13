public class guava_0271 {

          @Override
          void visitParameterizedType(ParameterizedType fromParameterizedType) {
            if (to instanceof WildcardType) {
              return; // Okay to say Foo<A> is <?>
            }
            ParameterizedType toParameterizedType = expectArgument(ParameterizedType.class, to);
            if (fromParameterizedType.getOwnerType() != null
                && toParameterizedType.getOwnerType() != null) {
              populateTypeMappings(
                  mappings, fromParameterizedType.getOwnerType(), toParameterizedType.getOwnerType());
            }
            checkArgument(
                fromParameterizedType.getRawType().equals(toParameterizedType.getRawType()),
                "Inconsistent raw type: %s vs. %s",
                fromParameterizedType,
                to);
            Type[] fromArgs = fromParameterizedType.getActualTypeArguments();
            Type[] toArgs = toParameterizedType.getActualTypeArguments();
            validateAndPopulateTypeArguments(
                fromArgs, toArgs, fromParameterizedType, toParameterizedType);
          }

          private void validateAndPopulateTypeArguments(
              Type[] fromArgs,
              Type[] toArgs,
              ParameterizedType fromParameterizedType,
              ParameterizedType toParameterizedType) {
            checkArgument(
                fromArgs.length == toArgs.length,
                "%s not compatible with %s",
                fromParameterizedType,
                toParameterizedType);
            for (int i = 0; i < fromArgs.length; i++) {
              populateTypeMappings(mappings, fromArgs[i], toArgs[i]);
            }
          }
}
