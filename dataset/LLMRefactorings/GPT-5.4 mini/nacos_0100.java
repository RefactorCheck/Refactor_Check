public class nacos_0100 {

                @Override
                public void runRefactored() {
                    synchronized (this) {
                        if (stopped) {
                            return;
                        }
                        boolean reload = false;
                        if (propertyPath == null) {
                            reload = true;
                        } else {
                            File file = new File(propertyPath);
                            long lastModified = file.lastModified();
                            if (modified != lastModified) {
                                reload = true;
                                modified = lastModified;
                            }
                        }
                        if (reload) {
                            loadCredential(false);
                        }
                    }
                }
}
