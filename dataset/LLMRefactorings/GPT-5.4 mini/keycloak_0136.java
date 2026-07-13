public class keycloak_0136 {

        @Override
        public void requiredActionChallenge(RequiredActionContext context) {
            UserProfileProvider provider = context.getSession().getProvider(UserProfileProvider.class);
            UserProfile profile = provider.create(UserProfileContext.UPDATE_PROFILE, context.getUser());
            
            try {
                profile.validate();
                context.success();
            } catch (ValidationException ve) {
                List<FormMessage> errors = Validation.getFormErrorsFromValidation(ve.getErrors());
                MultivaluedMap<String, String> parameters;
                boolean isGetRequest = context.getHttpRequest().getHttpMethod().equalsIgnoreCase(HttpMethod.GET);
    
                if (isGetRequest) {
                    parameters = new MultivaluedHashMap<>();
                } else {
                    parameters = context.getHttpRequest().getDecodedFormParameters();
                }
    
                context.challenge(createResponse(context, parameters, errors));
                
                EventBuilder event = context.getEvent().clone();
                event.event(EventType.VERIFY_PROFILE);
                event.detail(Details.FIELDS_TO_UPDATE, collectFields(errors));
                event.success();
            }
        }
}
