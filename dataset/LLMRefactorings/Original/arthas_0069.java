public class arthas_0069 {

        public boolean isAvailable(){
            if (isConnected()) {
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
                    /* these aren't exact checks (a Socket can be half-open),
                       but since we usually require two-way data transfer,
                       we check these here too: */
                    if (_socket_.isInputShutdown()) {
                        return false;
                    }
                    if (_socket_.isOutputShutdown()) {
                        return false;
                    }
                    /* ignore the result, catch exceptions: */
                    _socket_.getInputStream();
                    _socket_.getOutputStream();
                }
                catch (IOException ioex)
                {
                    return false;
                }
                return true;
            } else {
                return false;
            }
        }
}
