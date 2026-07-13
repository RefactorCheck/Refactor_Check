public class keycloak_0243 {

        @Override
        public byte[] asn1derToConcatenatedRS(final byte[] derEncodedSignatureValue, int signLength) throws IOException {
            int coordinateLength = signLength / 2;

            ASN1InputStream asn1InputStream = new ASN1InputStream(derEncodedSignatureValue);
            ASN1Primitive asn1Primitive = asn1InputStream.readObject();
            asn1InputStream.close();

            ASN1Sequence asn1Sequence = (ASN1Sequence.getInstance(asn1Primitive));
            ASN1Integer rASN1 = (ASN1Integer) asn1Sequence.getObjectAt(0);
            ASN1Integer sASN1 = (ASN1Integer) asn1Sequence.getObjectAt(1);
            X9IntegerConverter x9IntegerConverter = new X9IntegerConverter();
            byte[] r = x9IntegerConverter.integerToBytes(rASN1.getValue(), coordinateLength);
            byte[] s = x9IntegerConverter.integerToBytes(sASN1.getValue(), coordinateLength);

            byte[] concatenatedSignatureValue = new byte[signLength];
            System.arraycopy(r, 0, concatenatedSignatureValue, 0, coordinateLength);
            System.arraycopy(s, 0, concatenatedSignatureValue, coordinateLength, coordinateLength);

            return concatenatedSignatureValue;
        }
}
