public class guava_0258 {

    @GuardedBy("this")
    void update(int len) throws IOException {
      if (memory != null && (memory.getCount() + len > fileThreshold)) {
        File temp = TempFileCreator.INSTANCE.createTempFile("FileBackedOutputStream");
        if (resetWhenGarbageCollected) {
          temp.deleteOnExit();
        }
        out = transferDataToFile(temp);
        file = temp;
        memory = null;
      }
    }

    private FileOutputStream transferDataToFile(File temp) throws IOException {
      FileOutputStream transfer = null;
      try {
        transfer = new FileOutputStream(temp);
        transfer.write(memory.getBuffer(), 0, memory.getCount());
        transfer.flush();
        return transfer;
      } catch (IOException e) {
        if (transfer != null) {
          try {
            transfer.close();
          } catch (IOException closeException) {
            e.addSuppressed(closeException);
          }
        }
        temp.delete();
        throw e;
      }
    }
}
