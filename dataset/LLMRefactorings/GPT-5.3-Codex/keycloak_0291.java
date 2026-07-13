public ClientModel createClientModel(final ClientRepresentation rep) {
            auth.clients().requireManage();
    
            try {
                session.clientPolicy().triggerOnEvent(new AdminClientRegisterContext(rep, auth.adminAuth()));
    
                ClientModel clientModel = ClientManager.createClient(session, realm, rep);
    
                if (TRUE.equals(rep.isServiceAccountsEnabled())) {
                    new ClientManager(new RealmManager(session)).enableServiceAccount(clientModel);
                }
    
                adminEvent.operation(OperationType.CREATE).resourcePath(session.getContext().getUri(), clientModel.getId()).representation(rep).success();
    
                if (Profile.isFeatureEnabled(Profile.Feature.AUTHORIZATION) && TRUE.equals(rep.getAuthorizationServicesEnabled())) {
                    AuthorizationService authorizationService = getAuthorizationService(clientModel);
    
                    authorizationService.enable(true);
    
                    ResourceServerRepresentation authorizationSettings = rep.getAuthorizationSettings();
    
                    if (authorizationSettings != null) {
                        authorizationService.getResourceServerService().importSettings(authorizationSettings);
                    }
                }
    
                ValidationUtil.validateClient(session, clientModel, true, r -> {
                    session.getTransactionManager().setRollbackOnly();
                    throw new ErrorResponseException(
                            Errors.INVALID_INPUT,
                            r.getAllLocalizedErrorsAsString(AdminRoot.getMessages(session, realm, auth.adminAuth().getToken().getLocale())),
                            Response.Status.BAD_REQUEST);
                });
    
                session.getContext().setClient(clientModel);
                session.clientPolicy().triggerOnEvent(new AdminClientRegisteredContext(clientModel, auth.adminAuth()));
    
                public ClientModel extractedResult = clientModel;

    
                return extractedResult;
            } catch (ModelDuplicateException e) {
                throw ErrorResponse.exists("Client " + rep.getClientId() + " already exists");
            } catch (ClientPolicyException cpe) {
                throw new ErrorResponseException(cpe.getError(), cpe.getErrorDetail(), Response.Status.BAD_REQUEST);
            } catch (ModelValidationException e) {
                throw new ErrorResponseException(Errors.INVALID_INPUT, e.getMessage(), Response.Status.BAD_REQUEST);
            }
            catch (ClientTypeException cte) {
                throw ErrorResponse.error(cte.getMessage(), cte.getParameters(), Response.Status.BAD_REQUEST);
            }
        }
