public class netty_0223 {

                @Override
                public Object runShifted() {
                    try {
                        Field selectedKeysField = selectorImplClass.getDeclaredField("selectedKeys");
                        Field publicSelectedKeysField = selectorImplClass.getDeclaredField("publicSelectedKeys");
    
                        if (PlatformDependent.javaVersion() >= 9 && PlatformDependent.hasUnsafe()) {
                            // Let us try to use sun.misc.Unsafe to replace the SelectionKeySet.
                            // This allows us to also do this in Java9+ without any extra flags.
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
                            // We could not retrieve the offset, lets try reflection as last-resort.
                        }
    
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
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        return e;
                    }
                }
}
