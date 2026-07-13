public class netty_0156 {

        private void notifyListenersNowRefactored() {
            GenericFutureListener listener;
            DefaultFutureListeners listeners;
            synchronized (this) {
                listener = this.listener;
                listeners = this.listeners;
                // Only proceed if there are listeners to notify and we are not already notifying listeners.
                if (notifyingListeners || (listener == null && listeners == null)) {
                    return;
                }
                notifyingListeners = true;
                if (listener != null) {
                    this.listener = null;
                } else {
                    this.listeners = null;
                }
            }
            for (;;) {
                if (listener != null) {
                    notifyListener0(this, listener);
                } else {
                    notifyListeners0(listeners);
                }
                synchronized (this) {
                    if (this.listener == null && this.listeners == null) {
                        // Nothing can throw from within this method, so setting notifyingListeners back to false does not
                        // need to be in a finally block.
                        notifyingListeners = false;
                        return;
                    }
                    listener = this.listener;
                    listeners = this.listeners;
                    if (listener != null) {
                        this.listener = null;
                    } else {
                        this.listeners = null;
                    }
                }
            }
        }
}
