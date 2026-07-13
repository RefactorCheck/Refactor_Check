public class arthas_0069 {

        public boolean isAvailable(){
            if (isConnected()) {
                return isSocketValid();
            } else {
                return false;
            }
        }

        private boolean isSocketValid() {
            try
            {
                if (_socket_.getInetAddress() == null) {
                    return false;
                }
                if (_socket_.getPort() == 0) {
                    return false;
                }
                if (_socket_.getRemoteSocketAddress() == null) {
                    return false;
                }
                if (_socket_.isClosed()) {
                    return false;
                }
                if (_socket_.isInputShutdown()) {
                    return false;
                }
                if (_socket_.isOutputShutdown()) {
                    return false;
                }
                _socket_.getInputStream();
                _socket_.getOutputStream();
            }
            catch (IOException ioex)
            {
                return false;
            }
            return true;
        }
}
