public class arthas_0105 {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        if (className == null) {
            return null;
        }

        className = className.replace('/', '.');

        List<RetransformEntry> allRetransformEntries = allRetransformEntries();
        ListIterator<RetransformEntry> listIterator = allRetransformEntries
                .listIterator(allRetransformEntries.size());
        while (listIterator.hasPrevious()) {
            RetransformEntry retransformEntry = listIterator.previous();
            int id = retransformEntry.getId();
            if (matchesEntry(retransformEntry, className, loader)) {
                logger.info("RetransformCommand match class: {}, id: {}, classLoaderClass: {}, hashCode: {}",
                        className, id, retransformEntry.getClassLoaderClass(), retransformEntry.getHashCode());
                retransformEntry.incTransformCount();
                return retransformEntry.getBytes();
            }
        }

        return null;
    }

    private boolean matchesEntry(RetransformEntry retransformEntry, String className, ClassLoader loader) {
        if (!className.equals(retransformEntry.getClassName())) {
            return false;
        }
        if (retransformEntry.getClassLoaderClass() != null || retransformEntry.getHashCode() != null) {
            return isLoaderMatch(retransformEntry, loader);
        }
        return true;
    }
}
