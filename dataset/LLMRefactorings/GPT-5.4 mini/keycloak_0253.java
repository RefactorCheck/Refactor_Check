public class keycloak_0253 {

        public List<String> getCRLDistributionPoints(X509Certificate cert) throws IOException {
            byte[] extensionValue = cert.getExtensionValue(CRL_DISTRIBUTION_POINTS_OID);
            if (extensionValue == null) {
                return Collections.emptyList();
            }

            List<String> distributionPointUrls = new LinkedList<>();
            DEROctetString octetString;
            try (ASN1InputStream crldpExtensionInputStream = new ASN1InputStream(new ByteArrayInputStream(extensionValue))) {
                octetString = (DEROctetString)crldpExtensionInputStream.readObject();
            }
            byte[] octets = octetString.getOctets();

            CRLDistPoint crlDP;
            try (ASN1InputStream crldpInputStream = new ASN1InputStream(new ByteArrayInputStream(octets))) {
                crlDP = CRLDistPoint.getInstance(crldpInputStream.readObject());
            }

            for (DistributionPoint dp : crlDP.getDistributionPoints()) {
                DistributionPointName dpn = dp.getDistributionPoint();
                if (dpn != null && dpn.getType() == DistributionPointName.FULL_NAME) {
                    GeneralName[] names = GeneralNames.getInstance(dpn.getName()).getNames();
                    for (GeneralName gn : names) {
                        if (gn.getTagNo() == GeneralName.uniformResourceIdentifier) {
                            String url = DERIA5String.getInstance(gn.getName()).getString();
                            distributionPointUrls.add(url);
                        }
                    }
                }
            }

            return distributionPointUrls;
        }
}
