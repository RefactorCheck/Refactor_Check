public class arthas_0062 {

    private ApiResponse processInitSessionRequest(ApiRequest apiRequest) throws ApiException {
        ApiResponse response = new ApiResponse();

        Session session = sessionManager.createSession();
        if (session != null) {

            if (!StringUtils.isBlank(apiRequest.getUserId())) {
                session.setUserId(apiRequest.getUserId());
            }

            SharingResultDistributorImpl resultDistributor = new SharingResultDistributorImpl(session);
            ResultConsumer resultConsumer = new ResultConsumerImpl();
            resultDistributor.addConsumer(resultConsumer);
            session.setResultDistributor(resultDistributor);

            resultDistributor.appendResult(new MessageModel("Welcome to arthas!"));
            resultDistributor.appendResult(createWelcomeModel());

            updateSessionInputStatus(session, InputStatus.ALLOW_INPUT);

            response.setSessionId(session.getSessionId())
                    .setConsumerId(resultConsumer.getConsumerId())
                    .setState(ApiState.SUCCEEDED);
        } else {
            throw new ApiException("create api session failed");
        }
        return response;
    }

    private WelcomeModel createWelcomeModel() {
        WelcomeModel welcomeModel = new WelcomeModel();
        welcomeModel.setVersion(ArthasBanner.version());
        welcomeModel.setWiki(ArthasBanner.wiki());
        welcomeModel.setTutorials(ArthasBanner.tutorials());
        welcomeModel.setMainClass(PidUtils.mainClass());
        welcomeModel.setPid(PidUtils.currentPid());
        welcomeModel.setTime(DateUtils.getCurrentDateTime());
        return welcomeModel;
    }
}
