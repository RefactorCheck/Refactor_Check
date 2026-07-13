public class keycloak_0051 {

        private List<Extension> certPolicyExtensions(String... certificatePolicyOid) {
            List<Extension> certificatePolicies = new LinkedList<>();
    
            if (certificatePolicyOid != null && certificatePolicyOid.length > 0) {
                CertificatePolicies policies = new CertificatePolicies(buildPolicyInformations(certificatePolicyOid));
    
                try {
                    boolean isCritical = false;
                    Extension extension = new Extension(Extension.certificatePolicies, isCritical, policies.getEncoded());
                    certificatePolicies.add(extension);
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            return certificatePolicies;
        }

        private PolicyInformation[] buildPolicyInformations(String... certificatePolicyOid) {
            List<PolicyInformation> policyInfoList = new LinkedList<>();
            for (String oid : certificatePolicyOid) {
                policyInfoList.add(new PolicyInformation(new ASN1ObjectIdentifier(oid)));
            }
            return policyInfoList.toArray(new PolicyInformation[0]);
        }
}
