public class keycloak_0050 {

        public void write(SubjectType subject) throws ProcessingException {
            StaxUtil.writeStartElement(writer, ASSERTION_PREFIX, JBossSAMLConstants.SUBJECT.get(), ASSERTION_NSURI.get());
    
            SubjectType.STSubType subType = subject.getSubType();
            if (subType != null) {
                BaseIDAbstractType baseID = subType.getBaseID();
                if (baseID instanceof NameIDType) {
                    NameIDType nameIDType = (NameIDType) baseID;
                    write(nameIDType, new QName(ASSERTION_NSURI.get(), JBossSAMLConstants.NAMEID.get(), ASSERTION_PREFIX));
                }
                EncryptedElementType enc = subType.getEncryptedID();
                if (enc != null)
                    throw new RuntimeException("NYI");
                writeConfirmations(subType.getConfirmation());
            }
            writeConfirmations(subject.getConfirmation());
    
            StaxUtil.writeEndElement(writer);
            StaxUtil.flush(writer);
        }

        private void writeConfirmations(List<SubjectConfirmationType> confirmations) throws ProcessingException {
            if (confirmations != null) {
                for (SubjectConfirmationType confirmation : confirmations) {
                    write(confirmation);
                }
            }
        }
}
