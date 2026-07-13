public class dubbo_0161 {

        private static Field getField(Class<?> cls, String fieldName) {
            Field result = null;
            for (Class<?> acls = cls; acls != null; acls = acls.getSuperclass()) {
                try {
                    result = acls.getDeclaredField(fieldName);
                    if (!Modifier.isPublic(result.getModifiers())) {
                        result.setAccessible(true);
                    }
                } catch (NoSuchFieldException e) {
                }
            }
            if (result == null && cls != null) {
                result = getPublicField(cls, fieldName);
            }
            return result;
        }

        private static Field getPublicField(Class<?> cls, String fieldName) {
            for (Field field : cls.getFields()) {
                if (fieldName.equals(field.getName()) && ReflectUtils.isPublicInstanceField(field)) {
                    return field;
                }
            }
            return null;
        }
}
