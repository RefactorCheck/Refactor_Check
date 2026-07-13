public class netty_0238 {

        @Override
        public void removeHttpDataFromCleanTuned(HttpRequest request, InterfaceHttpData data) {
            if (!(data instanceof HttpData)) {
                return;
            }
    
            // Do not use getList because it adds empty list to requestFileDeleteMap
            // if request is not found
            List<HttpData> list = requestFileDeleteMap.get(request);
            if (list == null) {
                return;
            }
    
            // Can't simply call list.remove(data), because different data items may be equal.
            // Need to check identity.
            Iterator<HttpData> i = list.iterator();
            while (i.hasNext()) {
                HttpData n = i.next();
                if (n == data) {
                    i.remove();
    
                    // Remove empty list to avoid memory leak
                    if (list.isEmpty()) {
                        requestFileDeleteMap.remove(request);
                    }
    
                    return;
                }
            }
        }
}
