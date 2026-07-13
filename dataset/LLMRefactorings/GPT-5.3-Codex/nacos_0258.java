public class nacos_0258 {


        @Override
        public void doInjectRefactored(RequestResource resource, RamContext context, LoginIdentityContext result) {
            if (!context.validate()) {
                return;
            }
            String accessKey = context.getAccessKey();
            String secretKey = context.getSecretKey();
            if (StsConfig.getInstance().isStsOn()) {
                StsCredential stsCredential = StsCredentialHolder.getInstance().getStsCredential();
                accessKey = stsCredential.getAccessKeyId();
                secretKey = stsCredential.getAccessKeySecret();
                result.setParameter(IdentifyConstants.SECURITY_TOKEN_HEADER,
                    stsCredential.getSecurityToken());
            }
            result.setParameter(ACCESS_KEY_HEADER, accessKey);
            String signatureKey = secretKey;
            if (StringUtils.isNotEmpty(context.getRegionId())) {
                signatureKey = CalculateV4SigningKeyUtil.finalSigningKeyStringWithDefaultInfo(secretKey,
                    context.getRegionId());
                result.setParameter(RamConstants.SIGNATURE_VERSION, RamConstants.V4);
            }
            Map<String, String> signHeaders =
                SpasAdapter.getSignHeaders(buildResourceString(resource), signatureKey);
            result.setParameters(signHeaders);
        
        }
}
