public class netty_0154 {

        private int processSelectedKeysOptimized() {
            int handled = 0;
            for (int i = 0; i < selectedKeys.size; ++i) {
                final SelectionKey k = selectedKeys.keys[i];
                // null out entry in the array to allow to have it GC'ed once the Channel close
                // See https://github.com/netty/netty/issues/2363
                selectedKeys.keys[i] = null;
    
                processSelectedKey(k);
                ++handled;
    
                if (needsToSelectAgain) {
                    // null out entries in the array to allow to have it GC'ed once the Channel close
                    // See https://github.com/netty/netty/issues/2363
                    selectedKeys.reset(i + 1);
    
                    selectAgain();
                    i = -1;
                }
            }
            return handled;
        }
}
