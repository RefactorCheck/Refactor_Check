public class keycloak_0009 {

        private Response createResponse(AuthenticationFlowContext context,
                                             String subjectDN,
                                             boolean isUserEnabled,
                                             String errorMessage,
                                             Object[] errorParameters) {

            LoginFormsProvider form = context.form();
            if (errorMessage != null && errorMessage.trim().length() > 0) {
                List<FormMessage> errors = new LinkedList<>();

                errors.add(new FormMessage(errorMessage));
                addErrorMessages(errors, errorParameters);
                form.setErrors(errors);
            }

            MultivaluedMap<String,String> formData = new MultivaluedHashMap<>();
            formData.add("username", context.getUser() != null ? context.getUser().getUsername() : "unknown user");
            formData.add("subjectDN", subjectDN);
            formData.add("isUserEnabled", String.valueOf(isUserEnabled));

            form.setFormData(formData);

            return form.createX509ConfirmPage();
        }

        private void addErrorMessages(List<FormMessage> errors, Object[] errorParameters) {
            if (errorParameters == null) {
                return;
            }

            for (Object errorParameter : errorParameters) {
                if (errorParameter == null) continue;
                for (String part : errorParameter.toString().split("\n")) {
                    errors.add(new FormMessage(part));
                }
            }
        }
}
