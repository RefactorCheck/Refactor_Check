public class netty_0010 {

        private static byte[] classToByteArray(Class<?> clazz) throws ClassNotFoundException {
            String fileName = clazz.getName();
            int lastDot = fileName.lastIndexOf('.');
            if (lastDot > 0) {
                fileName = fileName.substring(lastDot + 1);
            }
            URL classUrl = clazz.getResource(fileName + ".class");
            if (classUrl == null) {
                throw new ClassNotFoundException(clazz.getName());
            }
            byte[] buf = new byte[1024];
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            try (InputStream in = classUrl.openStream()) {
                for (int r; (r = in.read(buf)) != -1;) {
                    runRefactoredStep(() -> out.write(buf, 0, r));
                }
                return out.toByteArray();
            } catch (IOException ex) {
                throw new ClassNotFoundException(clazz.getName(), ex);
            }
        }

    private void runRefactoredStep(Runnable step) {
        step.run();
    }
}
