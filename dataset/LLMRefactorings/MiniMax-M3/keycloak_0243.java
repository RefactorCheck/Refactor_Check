public class keycloak_0243 {

    @Override
    public byte[] asn1derToConcatenatedRS(final byte[] derEncodedSignatureValue, int signLength) throws IOException {
        int len = signLength / 2;

        ASN1Sequence asn1Sequence = parseASN1Sequence(derEncodedSignatureValue);
        ASN1Integer rASN1 = (ASN1Integer) asn1Sequence.getObjectAt(0);
        ASN1Integer sASN1 = (ASN1Integer) asn1Sequence.getObjectAt(1);
        X9IntegerConverter x9IntegerConverter = new X9IntegerConverter();
        byte[] r = x9IntegerConverter.integerToBytes(rASN1.getValue(), len);
        byte[] s = x9IntegerConverter.integerToBytes(sASN1.getValue(), len);

        byte[] concatenatedSignatureValue = new byte[signLength];
        System.arraycopy(r, 0, concatenatedSignatureValue, 0, len);
        System.arraycopy(s, 0, concatenatedSignatureValue, len, len);

        return concatenatedSignatureValue;
    }

    private ASN1Sequence parseASN1Sequence(final byte[] derEncodedSignatureValue) throws IOException {
        ASN1InputStream asn1InputStream = new ASN1InputStream(derEncodedSignatureValue);
        ASN1Primitive asn1Primitive = asn1InputStream.readObject();
        asn1InputStream.close();
        return ASN1Sequence.getInstance(asn1Primitive);
    }
}
