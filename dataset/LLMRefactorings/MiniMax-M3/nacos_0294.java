public class nacos_0294 {

    @Since("3.0.0")
    @PostMapping("/login")
    public Object login(HttpServletResponse response, HttpServletRequest request)
        throws AccessException, IOException {
        if (isNacosOrLdapAuth()) {
            NacosUser user = iAuthenticationManager.authenticate(request);
            response.addHeader(AuthConstants.AUTHORIZATION_HEADER,
                AuthConstants.TOKEN_PREFIX + user.getToken());
            return buildLoginResult(user);
        }
        return Result.failure(ErrorCode.ILLEGAL_STATE.getCode(),
            "Current Nacos auth plugin type is not `nacos` or `nacos-ldap`, don't support login API.",
            null);
    }

    private boolean isNacosOrLdapAuth() {
        return AuthSystemTypes.NACOS.name().equalsIgnoreCase(authConfigs.getNacosAuthSystemType())
            || AuthSystemTypes.LDAP.name().equalsIgnoreCase(authConfigs.getNacosAuthSystemType());
    }

    private Map<String, Object> buildLoginResult(NacosUser user) {
        Map<String, Object> result = new HashMap<>();
        result.put(Constants.ACCESS_TOKEN, user.getToken());
        result.put(Constants.TOKEN_TTL, jwtTokenManager.getTokenTtlInSeconds(user.getToken()));
        result.put(Constants.GLOBAL_ADMIN, iAuthenticationManager.hasGlobalAdminRole(user));
        result.put(Constants.USERNAME, user.getUserName());
        return result;
    }
}
