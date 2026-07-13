public class nacos_0276 {

        public void validateToken(String token) throws AccessException {
            if (tokenMap.get(token) != null) {
                return;
            }
            parseAndCacheToken(token);
        }

        private void parseAndCacheToken(String token) throws AccessException {
            jwtTokenManager.validateToken(token);
            Authentication authentication = jwtTokenManager.getAuthentication(token);
            String username = authentication.getName();
            if (username == null || username.isEmpty()) {
                return;
            }
            long expiredTime =
                TimeUnit.SECONDS.toMillis(jwtTokenManager.getExpiredTimeInSeconds(token));
            if (expiredTime <= System.currentTimeMillis()) {
                return;
            }
            NacosUser user = jwtTokenManager.parseToken(token);
            tokenMap.putIfAbsent(token,
                new TokenEntity(token, username, expiredTime, authentication, user));
        }
}
