public class netty_0223 {

    @Override
    public Object run() {
        try {
            Field selectedKeysField = selectorImplClass.getDeclaredField("selectedKeys");
            Field publicSelectedKeysField = selectorImplClass.getDeclaredField("publicSelectedKeys");

            if (PlatformDependent.javaVersion() >= 9 && PlatformDependent.hasUnsafe()) {
                long selectedKeysFieldOffset = PlatformDependent.objectFieldOffset(selectedKeysField);
                long publicSelectedKeysFieldOffset =
                        PlatformDependent.objectFieldOffset(publicSelectedKeysField);

                if (selectedKeysFieldOffset != -1 && publicSelectedKeysFieldOffset != -1) {
                    PlatformDependent.putObject(
                            unwrappedSelector, selectedKeysFieldOffset, selectedKeySet);
                    PlatformDependent.putObject(
                            unwrappedSelector, publicSelectedKeysFieldOffset, selectedKeySet);
                    return null;
                }
            }

            return setSelectedKeysViaReflection(selectedKeysField, publicSelectedKeysField);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return e;
        }
    }

    private Object setSelectedKeysViaReflection(Field selectedKeysField, Field publicSelectedKeysField)
            throws IllegalAccessException {
        Throwable cause = ReflectionUtil.trySetAccessible(selectedKeysField, true);
        if (cause != null) {
            return cause;
        }
        cause = ReflectionUtil.trySetAccessible(publicSelectedKeysField, true);
        if (cause != null) {
            return cause;
        }

        selectedKeysField.set(unwrappedSelector, selectedKeySet);
        publicSelectedKeysField.set(unwrappedSelector, selectedKeySet);
        return null;
    }
}
