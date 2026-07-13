public class nacos_0279 {


        private static void checkParameterizeMethodParameter(Class<?> raw, final Type... typeArguments) {
            if (raw == null) {
                throw new NullPointerException("raw cannot be null");
            }
            if (typeArguments == null) {
                throw new NullPointerException("typeArguments cannot be null");
            }
            if (typeArguments.length != raw.getTypeParameters().length) {
                throw new IllegalArgumentException(
                    String.format("invalid number of type parameters specified: expected %s, got %s",
                        raw.getTypeParameters().length, typeArguments.length));
            }
            
            for (int i = 0; i < typeArguments.length; i++) {
                if (typeArguments[i] == null) {
                    throw new IllegalArgumentException("There can be no null in typeArguments");
                }
            }
            
        
        }
}
