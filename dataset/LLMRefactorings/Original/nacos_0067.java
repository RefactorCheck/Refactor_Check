public class nacos_0067 {

        @Since("3.0.0")
        @PostMapping("/admin")
        public Result<User> createAdminUser(@RequestParam(required = false) String password) {
            
            if (StringUtils.isBlank(password)) {
                password = PasswordGeneratorUtil.generateRandomPassword();
            }
            
            if (AuthSystemTypes.NACOS.name().equalsIgnoreCase(authConfigs.getNacosAuthSystemType())) {
                if (iAuthenticationManager.hasGlobalAdminRole()) {
                    return Result.failure(HttpStatus.CONFLICT.value(), "have admin user cannot use it.",
                        null);
                }
                String username = AuthConstants.DEFAULT_USER;
                userDetailsService.createUser(username, password);
                roleService.addAdminRole(username);
                User result = new User();
                result.setUsername(username);
                result.setPassword(password);
                return Result.success(result);
            } else {
                return Result.failure(HttpStatus.NOT_IMPLEMENTED.value(),
                    "Current auth type not supported create admin user.", null);
            }
        }
}
