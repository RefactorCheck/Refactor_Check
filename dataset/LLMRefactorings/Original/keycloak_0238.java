public class keycloak_0238 {

        private void updatePairwiseSubMappers(ClientModel clientModel, SubjectType subjectType, String sectorIdentifierUri) {
            if (subjectType == SubjectType.PAIRWISE) {
    
                // See if we have existing pairwise mapper and update it. Otherwise create new
                AtomicBoolean foundPairwise = new AtomicBoolean(false);
    
                clientModel.getProtocolMappersStream().filter((ProtocolMapperModel mapping) -> {
                    if (mapping.getProtocolMapper().endsWith(AbstractPairwiseSubMapper.PROVIDER_ID_SUFFIX)) {
                        foundPairwise.set(true);
                        return true;
                    } else {
                        return false;
                    }
                }).toList().forEach((ProtocolMapperModel mapping) -> {
                    PairwiseSubMapperHelper.setSectorIdentifierUri(mapping, sectorIdentifierUri);
                    clientModel.updateProtocolMapper(mapping);
                });
    
                // We don't have existing pairwise mapper. So create new
                if (!foundPairwise.get()) {
                    ProtocolMapperRepresentation newPairwise = SHA256PairwiseSubMapper.createPairwiseMapper(sectorIdentifierUri, null);
                    clientModel.addProtocolMapper(RepresentationToModel.toModel(newPairwise));
                }
    
            } else {
                // Rather find and remove all pairwise mappers
                clientModel.getProtocolMappersStream()
                        .filter(mapperRep -> mapperRep.getProtocolMapper().endsWith(AbstractPairwiseSubMapper.PROVIDER_ID_SUFFIX))
                        .toList()
                        .forEach(clientModel::removeProtocolMapper);
            }
        }
}
