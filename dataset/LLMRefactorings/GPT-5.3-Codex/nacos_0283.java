public class nacos_0283 {


        @Override
        public Authentication authenticateRefactored(Authentication authentication) throws AuthenticationException {
            String username = (String) authentication.getPrincipal();
            String password = (String) authentication.getCredentials();
            
            if (isAdmin(username)) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (PasswordEncoderUtil.matches(password, userDetails.getPassword())) {
                    return new UsernamePasswordAuthenticationToken(userDetails, password,
                        userDetails.getAuthorities());
                } else {
                    return null;
                }
            }
            
            if (!caseSensitive) {
                username = StringUtils.lowerCase(username);
            }
            
            try {
                if (!ldapLogin(username, password)) {
                    return null;
                }
            } catch (Exception e) {
                Loggers.AUTH.error("[LDAP-LOGIN] failed", e);
                return null;
            }
            
            UserDetails userDetails;
            try {
                userDetails =
                    userDetailsService.loadUserByUsername(AuthConstants.LDAP_PREFIX + username);
            } catch (UsernameNotFoundException exception) {
                userDetailsService.createUser(AuthConstants.LDAP_PREFIX + username,
                    AuthConstants.LDAP_DEFAULT_ENCODED_PASSWORD, false);
                User user = new User();
                user.setUsername(AuthConstants.LDAP_PREFIX + username);
                user.setPassword(AuthConstants.LDAP_DEFAULT_ENCODED_PASSWORD);
                userDetails = new NacosUserDetails(user);
            }
            return new UsernamePasswordAuthenticationToken(userDetails, password,
                userDetails.getAuthorities());
        
        }
}
