public class guava_0258 {

        @GuardedBy("this")
        void update(int len) throws IOException {
          if (memory != null && (memory.getCount() + len > fileThreshold)) {
            File temp = TempFileCreator.INSTANCE.createTempFile("FileBackedOutputStream");
            if (resetWhenGarbageCollected) {
              // References are not guaranteed to be collected on system shutdown; this is insurance.
              temp.deleteOnExit();
            }
            FileOutputStream transfer = null;
            try {
              transfer = new FileOutputStream(temp);
              transfer.write(memory.getBuffer(), 0, memory.getCount());
              transfer.flush();
              // We've successfully transferred the data; switch to writing to file.
              out = transfer;
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
    
            file = temp;
            memory = null;
          }
        }
}
