public class keycloak_0051 {

        private List<Extension> certPolicyExtensions(String... certificatePolicyOid) {
            List<Extension> certificatePolicies = new LinkedList<>();

            if (certificatePolicyOid != null && certificatePolicyOid.length > 0) {
                List<PolicyInformation> policyInfoList = new LinkedList<>();
                for (String oid : certificatePolicyOid) {
                    policyInfoList.add(new PolicyInformation(new ASN1ObjectIdentifier(oid)));
                }

                CertificatePolicies policies = new CertificatePolicies(policyInfoList.toArray(new PolicyInformation[0]));

                try {
                    boolean isCritical = false;
                    byte[] encodedPolicies = policies.getEncoded();
                    Extension extension = new Extension(Extension.certificatePolicies, isCritical, encodedPolicies);
                    certificatePolicies.add(extension);
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            return certificatePolicies;
        }
}
