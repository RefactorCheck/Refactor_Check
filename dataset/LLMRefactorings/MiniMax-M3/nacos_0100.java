public class nacos_0100 {

                @Override
                public void run() {
                    synchronized (this) {
                        if (stopped) {
                            return;
                        }
                        if (shouldReload()) {
                            loadCredential(false);
                        }
                    }
                }

                private boolean shouldReload() {
                    if (propertyPath == null) {
                        return true;
                    }
                    File file = new File(propertyPath);
                    long lastModified = file.lastModified();
                    if (modified != lastModified) {
                        modified = lastModified;
                        return true;
                    }
                    return false;
                }
}
