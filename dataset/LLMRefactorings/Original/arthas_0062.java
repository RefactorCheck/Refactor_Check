public class arthas_0062 {

        private ApiResponse processInitSessionRequest(ApiRequest apiRequest) throws ApiException {
            ApiResponse response = new ApiResponse();
    
            //create session
            Session session = sessionManager.createSession();
            if (session != null) {
    
                // set userId if provided
                if (!StringUtils.isBlank(apiRequest.getUserId())) {
                    session.setUserId(apiRequest.getUserId());
                }
    
                //Result Distributor
                SharingResultDistributorImpl resultDistributor = new SharingResultDistributorImpl(session);
                //create consumer
                ResultConsumer resultConsumer = new ResultConsumerImpl();
                resultDistributor.addConsumer(resultConsumer);
                session.setResultDistributor(resultDistributor);
    
                resultDistributor.appendResult(new MessageModel("Welcome to arthas!"));
    
                //welcome message
                WelcomeModel welcomeModel = new WelcomeModel();
                welcomeModel.setVersion(ArthasBanner.version());
                welcomeModel.setWiki(ArthasBanner.wiki());
                welcomeModel.setTutorials(ArthasBanner.tutorials());
                welcomeModel.setMainClass(PidUtils.mainClass());
                welcomeModel.setPid(PidUtils.currentPid());
                welcomeModel.setTime(DateUtils.getCurrentDateTime());
                resultDistributor.appendResult(welcomeModel);
    
                //allow input
                updateSessionInputStatus(session, InputStatus.ALLOW_INPUT);
    
                response.setSessionId(session.getSessionId())
                        .setConsumerId(resultConsumer.getConsumerId())
                        .setState(ApiState.SUCCEEDED);
            } else {
                throw new ApiException("create api session failed");
            }
            return response;
        }
}
