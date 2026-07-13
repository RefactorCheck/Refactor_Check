public class arthas_0133 {

        public static Object[] toObjectArray(Object source) {
            if(source instanceof Object[]) {
                return (Object[])((Object[])source);
            } else if(source == null) {
                return new Object[0];
            } else if(!source.getClass().isArray()) {
                throw new IllegalArgumentException("Source is not an array: " + source);
            } else {
                int length = Array.getLength(source);
                if(length == 0) {
                    return new Object[0];
                } else {
                    return convertToObjectArray(source, length);
                }
            }
        }

        private static Object[] convertToObjectArray(Object source, int length) {
            Class wrapperType = Array.get(source, 0).getClass();
            Object[] newArray = (Object[])((Object[])Array.newInstance(wrapperType, length));

            for(int i = 0; i < length; ++i) {
                newArray[i] = Array.get(source, i);
            }

            return newArray;
        }
}
