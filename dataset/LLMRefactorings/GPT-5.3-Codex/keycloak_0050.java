public void write(SubjectType samlSubject) throws ProcessingException {
            StaxUtil.writeStartElement(writer, ASSERTION_PREFIX, JBossSAMLConstants.SUBJECT.get(), ASSERTION_NSURI.get());
    
            SubjectType.STSubType subType = samlSubject.getSubType();
            if (subType != null) {
                BaseIDAbstractType baseID = subType.getBaseID();
                if (baseID instanceof NameIDType) {
                    NameIDType nameIDType = (NameIDType) baseID;
                    write(nameIDType, new QName(ASSERTION_NSURI.get(), JBossSAMLConstants.NAMEID.get(), ASSERTION_PREFIX));
                }
                EncryptedElementType enc = subType.getEncryptedID();
                if (enc != null)
                    throw new RuntimeException("NYI");
                List<SubjectConfirmationType> confirmations = subType.getConfirmation();
                if (confirmations != null) {
                    for (SubjectConfirmationType confirmation : confirmations) {
                        write(confirmation);
                    }
                }
            }
            List<SubjectConfirmationType> subjectConfirmations = samlSubject.getConfirmation();
            if (subjectConfirmations != null) {
                for (SubjectConfirmationType subjectConfirmationType : subjectConfirmations) {
                    write(subjectConfirmationType);
                }
            }
    
            StaxUtil.writeEndElement(writer);
            StaxUtil.flush(writer);
        }
