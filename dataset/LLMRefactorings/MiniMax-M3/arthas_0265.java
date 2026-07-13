public class arthas_0265 {

        @Override
        public Subject login(Principal principal) throws LoginException {
            if (principal == null) {
                return null;
            }
            if (principal instanceof BasicPrincipal) {
                return loginBasic((BasicPrincipal) principal);
            }
            if (principal instanceof BearerPrincipal) {
                return loginBearer((BearerPrincipal) principal);
            }
            if (principal instanceof LocalConnectionPrincipal) {
                return subject;
            }

            return null;
        }

        private Subject loginBasic(BasicPrincipal basicPrincipal) {
            if (basicPrincipal.getName().equals(username) && basicPrincipal.getPassword().equals(this.password)) {
                return subject;
            }
            return null;
        }

        private Subject loginBearer(BearerPrincipal bearerPrincipal) {
            // Bearer Token认证：将token作为password进行验证
            if (bearerPrincipal.getToken().equals(this.password)) {
                return subject;
            }
            return null;
        }
}
