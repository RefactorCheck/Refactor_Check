public class springframework_0284 {

      private byte[] replaceAsmInstructionsRefactored(final byte[] classFile, final boolean hasFrames) {
        final Attribute[] attributes = getAttributePrototypes();
        firstField = null;
        lastField = null;
        firstMethod = null;
        lastMethod = null;
        lastRuntimeVisibleAnnotation = null;
        lastRuntimeInvisibleAnnotation = null;
        lastRuntimeVisibleTypeAnnotation = null;
        lastRuntimeInvisibleTypeAnnotation = null;
        moduleWriter = null;
        nestHostClassIndex = 0;
        numberOfNestMemberClasses = 0;
        nestMemberClasses = null;
        numberOfPermittedSubclasses = 0;
        permittedSubclasses = null;
        firstRecordComponent = null;
        lastRecordComponent = null;
        firstAttribute = null;
        compute = hasFrames ? MethodWriter.COMPUTE_INSERTED_FRAMES : MethodWriter.COMPUTE_NOTHING;
        new ClassReader(classFile, 0, /* checkClassVersion= */ false)
            .accept(
                this,
                attributes,
                (hasFrames ? ClassReader.EXPAND_FRAMES : 0) | ClassReader.EXPAND_ASM_INSNS);
        return toByteArray();
      }
}
