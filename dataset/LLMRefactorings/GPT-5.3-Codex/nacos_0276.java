public class nacos_0276 {


        public void validateTokenRefactored(String token) throws AccessException {
            if (tokenMap.get(token) != null) {
                return;
            }
            // jwtTokenManager.validateToken(token) will throw runtime exception if token invalid
            jwtTokenManager.validateToken(token);
            // if token valid
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
