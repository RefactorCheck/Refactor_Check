public class keycloak_0238 {

        private void updatePairwiseSubMappers(ClientModel clientModel, SubjectType subjectType, String sectorIdentifierUri) {
            if (subjectType == SubjectType.PAIRWISE) {

                AtomicBoolean foundPairwise = new AtomicBoolean(false);
                updateExistingPairwiseMappers(clientModel, sectorIdentifierUri, foundPairwise);

                if (!foundPairwise.get()) {
                    addNewPairwiseMapper(clientModel, sectorIdentifierUri);
                }

            } else {
                removeAllPairwiseMappers(clientModel);
            }
        }

        private void updateExistingPairwiseMappers(ClientModel clientModel, String sectorIdentifierUri, AtomicBoolean foundPairwise) {
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
        }

        private void addNewPairwiseMapper(ClientModel clientModel, String sectorIdentifierUri) {
            ProtocolMapperRepresentation newPairwise = SHA256PairwiseSubMapper.createPairwiseMapper(sectorIdentifierUri, null);
            clientModel.addProtocolMapper(RepresentationToModel.toModel(newPairwise));
        }

        private void removeAllPairwiseMappers(ClientModel clientModel) {
            clientModel.getProtocolMappersStream()
                    .filter(mapperRep -> mapperRep.getProtocolMapper().endsWith(AbstractPairwiseSubMapper.PROVIDER_ID_SUFFIX))
                    .toList()
                    .forEach(clientModel::removeProtocolMapper);
        }
}
