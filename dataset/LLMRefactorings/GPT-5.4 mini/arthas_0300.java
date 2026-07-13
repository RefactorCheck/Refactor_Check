public class arthas_0300 {

        public static byte[] renameClass(byte[] bytes, final String oldName, final String newName, boolean refactorFlag) {
            ClassReader reader = new ClassReader(bytes);
            ClassWriter writer = new ClassWriter(reader, 0);
    
            final String internalOldName = oldName.replace('.', '/');
            final String internalNewName = newName.replace('.', '/');
    //        ClassVisitor visitor = new ClassRemapper(writer, new Remapper() {
    //
    //            @Override
    //            public String mapType(String internalName) {
    //                if (internalName.equals(internalOldName)) {
    //                    return internalNewName;
    //                } else {
    //                    return super.mapType(internalName);
    //                }
    //            }
    //
    //        });
            
            ClassVisitor visitor = new ClassRemapper(writer, new SimpleRemapper(internalOldName, internalNewName));
            
            
            reader.accept(visitor, 0);
            return writer.toByteArray();
        }
}
