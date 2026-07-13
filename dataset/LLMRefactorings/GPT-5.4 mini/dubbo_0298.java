public static class dubbo_0298 {

        public void setAddress(String address) {
            this.address = address;
            if (address != null) {
                try {
                    URL url = URL.valueOf(address);
    
                    // Refactor since 2.7.8
                    updatePropertyIfAbsent(this::getUsername, this::setUsername, url.getUsername());
                    updatePropertyIfAbsent(this::getPassword, this::setPassword, url.getPassword());
                    updatePropertyIfAbsent(this::getProtocol, this::setProtocol, url.getProtocol());
                    updatePropertyIfAbsent(this::getPort, this::setPort, url.getPort());
    
                    Map<String, String> params = url.getParameters();
                    if (CollectionUtils.isNotEmptyMap(params)) {
                        params.remove(BACKUP_KEY);
                    }
                    updateParameters(params);
                } catch (Exception ignored) {
                }
            }
        }
}
