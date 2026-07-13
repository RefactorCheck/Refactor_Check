@Override
        public byte[] asn1derToConcatenatedRS(final byte[] derEncodedSignatureValue, int signLength) throws IOException {
            int signaturePartLength = signLength / 2;
    
            ASN1InputStream asn1InputStream = new ASN1InputStream(derEncodedSignatureValue);
            ASN1Primitive asn1Primitive = asn1InputStream.readObject();
            asn1InputStream.close();
    
            ASN1Sequence asn1Sequence = (ASN1Sequence.getInstance(asn1Primitive));
            ASN1Integer rASN1 = (ASN1Integer) asn1Sequence.getObjectAt(0);
            ASN1Integer sASN1 = (ASN1Integer) asn1Sequence.getObjectAt(1);
            X9IntegerConverter x9IntegerConverter = new X9IntegerConverter();
            byte[] r = x9IntegerConverter.integerToBytes(rASN1.getValue(), signaturePartLength);
            byte[] s = x9IntegerConverter.integerToBytes(sASN1.getValue(), signaturePartLength);
    
            byte[] concatenatedSignatureValue = new byte[signLength];
            System.arraycopy(r, 0, concatenatedSignatureValue, 0, signaturePartLength);
            System.arraycopy(s, 0, concatenatedSignatureValue, signaturePartLength, signaturePartLength);
    
            return concatenatedSignatureValue;
        }
