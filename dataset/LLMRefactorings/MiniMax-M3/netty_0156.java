public class netty_0156 {

        private void notifyListenersNow() {
            GenericFutureListener listener;
            DefaultFutureListeners listeners;
            synchronized (this) {
                listener = this.listener;
                listeners = this.listeners;
                if (notifyingListeners || (listener == null && listeners == null)) {
                    return;
                }
                notifyingListeners = true;
                clearActiveListenerOrListeners();
            }
            for (;;) {
                if (listener != null) {
                    notifyListener0(this, listener);
                } else {
                    notifyListeners0(listeners);
                }
                synchronized (this) {
                    if (this.listener == null && this.listeners == null) {
                        notifyingListeners = false;
                        return;
                    }
                    listener = this.listener;
                    listeners = this.listeners;
                    clearActiveListenerOrListeners();
                }
            }
        }

        private void clearActiveListenerOrListeners() {
            if (this.listener != null) {
                this.listener = null;
            } else {
                this.listeners = null;
            }
        }
}
