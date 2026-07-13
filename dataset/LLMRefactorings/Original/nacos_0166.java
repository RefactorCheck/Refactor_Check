public class nacos_0166 {

        @Override
        public void addRole(String role, String username) {
            if (AuthConstants.GLOBAL_ADMIN_ROLE.equals(role)) {
                throw new IllegalArgumentException(
                    "role '" + AuthConstants.GLOBAL_ADMIN_ROLE + "' is not permitted to create!");
            }
            if (AuthConstants.ANONYMOUS_ROLE.equals(role)) {
                throw new IllegalArgumentException(
                    "role '" + AuthConstants.ANONYMOUS_ROLE + "' is reserved by the system");
            }
            Map<String, String> body = Map.of("role", role, "username", username);
            try {
                HttpRestResult<String> httpResult = nacosRestTemplate.postForm(
                    buildRemoteRoleUrlPath(AuthConstants.ROLE_PATH),
                    RemoteServerUtil.buildServerRemoteHeader(authConfigs), body, String.class);
                RemoteServerUtil.singleCheckResult(httpResult);
                getCachedRoleSet().add(role);
            } catch (NacosException e) {
                throw new NacosRuntimeException(e.getErrCode(), e.getErrMsg());
            } catch (Exception unpectedException) {
                throw new NacosRuntimeException(NacosException.SERVER_ERROR,
                    unpectedException.getMessage());
            }
        }
}
