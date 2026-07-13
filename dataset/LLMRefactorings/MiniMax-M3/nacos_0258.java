public class nacos_0258 {

        @Override
        public void doInject(RequestResource resource, RamContext context,
            LoginIdentityContext result) {
            if (!context.validate()) {
                return;
            }
            String[] credentials = resolveCredentials(context, result);
            String accessKey = credentials[0];
            String secretKey = credentials[1];
            result.setParameter(ACCESS_KEY_HEADER, accessKey);
            String signatureKey = computeSignatureKey(secretKey, context, result);
            Map<String, String> signHeaders =
                SpasAdapter.getSignHeaders(buildResourceString(resource), signatureKey);
            result.setParameters(signHeaders);
        }

        private String[] resolveCredentials(RamContext context, LoginIdentityContext result) {
            String accessKey = context.getAccessKey();
            String secretKey = context.getSecretKey();
            if (StsConfig.getInstance().isStsOn()) {
                StsCredential stsCredential = StsCredentialHolder.getInstance().getStsCredential();
                accessKey = stsCredential.getAccessKeyId();
                secretKey = stsCredential.getAccessKeySecret();
                result.setParameter(IdentifyConstants.SECURITY_TOKEN_HEADER,
                    stsCredential.getSecurityToken());
            }
            return new String[]{accessKey, secretKey};
        }

        private String computeSignatureKey(String secretKey, RamContext context,
                LoginIdentityContext result) {
            String signatureKey = secretKey;
            if (StringUtils.isNotEmpty(context.getRegionId())) {
                signatureKey = CalculateV4SigningKeyUtil.finalSigningKeyStringWithDefaultInfo(secretKey,
                    context.getRegionId());
                result.setParameter(RamConstants.SIGNATURE_VERSION, RamConstants.V4);
            }
            return signatureKey;
        }
}
