@POST
        @Path("{parentId}/mappers/{id}/sync")
        @NoCache
        @Produces(MediaType.APPLICATION_JSON)
        public SynchronizationResult syncMapperData(@PathParam("parentId") String parentId, @PathParam("id") String mapperId, @QueryParam("direction") String direction) {
            auth.users().requireManage();
    
            ComponentModel parentModel = realm.getComponent(parentId);
            if (parentModel == null) throw new NotFoundException("Parent model not found");
            ComponentModel mapperModel = realm.getComponent(mapperId);
            if (mapperModel == null) throw new NotFoundException("Mapper model not found");
    
            session.getProvider(UserStorageProvider.class, parentModel);
            LDAPStorageMapper ldapStorageMapper = session.getProvider(LDAPStorageMapper.class, mapperModel);
    
            ServicesLogger.LOGGER.syncingDataForMapper(mapperModel.getName(), mapperModel.getProviderId(), direction);
    
            SynchronizationResult syncResult;
            if ("fedToKeycloak".equals(direction)) {
                try {
                    syncResult = ldapStorageMapper.syncDataFromFederationProviderToKeycloak(realm);
                } catch(Exception e) {
                    String errorMsg = getErrorCode(e);
                    throw ErrorResponse.error(errorMsg, Response.Status.BAD_REQUEST);
                }
            } else if ("keycloakToFed".equals(direction)) {
                try {
                    syncResult = ldapStorageMapper.syncDataFromKeycloakToFederationProvider(realm);
                } catch(Exception e) {
                    String errorMsg = getErrorCode(e);
                    throw ErrorResponse.error(errorMsg, Response.Status.BAD_REQUEST);
                }
            } else {
                throw new BadRequestException("Unknown direction: " + direction);
            }
    
            Map<String, Object> eventRep = new HashMap<>();
            eventRep.put("action", direction);
            eventRep.put("result", syncResult);
            adminEvent.operation(OperationType.ACTION).resourcePath(session.getContext().getUri()).representation(eventRep).success();
            return syncResult;
        }
