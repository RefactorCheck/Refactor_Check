public class keycloak_0009 {

    private Response createResponse(AuthenticationFlowContext context,
                                     String subjectDN,
                                     boolean isUserEnabled,
                                     String errorMessage,
                                     Object[] errorParameters) {

        LoginFormsProvider form = context.form();
        List<FormMessage> errors = buildErrorMessages(errorMessage, errorParameters);
        if (errors != null) {
            form.setErrors(errors);
        }

        MultivaluedMap<String, String> formData = new MultivaluedHashMap<>();
        formData.add("username", context.getUser() != null ? context.getUser().getUsername() : "unknown user");
        formData.add("subjectDN", subjectDN);
        formData.add("isUserEnabled", String.valueOf(isUserEnabled));

        form.setFormData(formData);

        return form.createX509ConfirmPage();
    }

    private List<FormMessage> buildErrorMessages(String errorMessage, Object[] errorParameters) {
        if (errorMessage == null || errorMessage.trim().length() == 0) {
            return null;
        }
        List<FormMessage> errors = new LinkedList<>();
        errors.add(new FormMessage(errorMessage));
        if (errorParameters != null) {
            for (Object errorParameter : errorParameters) {
                if (errorParameter == null) continue;
                for (String part : errorParameter.toString().split("\n")) {
                    errors.add(new FormMessage(part));
                }
            }
        }
        return errors;
    }
}
