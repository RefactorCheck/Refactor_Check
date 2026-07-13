public class arthas_0265 {

        @Override
        public Subject login(Principal principal) throws LoginException {
            if (principal == null) {
                return null;
            }
            if (principal instanceof BasicPrincipal) {
                BasicPrincipal basicPrincipal = (BasicPrincipal) principal;
                if (basicPrincipal.getName().equals(username) && basicPrincipal.getPassword().equals(this.password)) {
                    return subject;
                }
            }
            if (principal instanceof BearerPrincipal) {
                BearerPrincipal bearerPrincipal = (BearerPrincipal) principal;
                // Bearer Token认证：将token作为password进行验证
                if (bearerPrincipal.getToken().equals(this.password)) {
                    return subject;
                }
            }
            if (principal instanceof LocalConnectionPrincipal) {
                return subject;
            }
    
            return null;
        }
}
