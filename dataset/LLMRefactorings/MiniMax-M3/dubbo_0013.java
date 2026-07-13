public class dubbo_0013 {

    private void filterSecuritySchemes(Components components, OpenAPIFilter[] filters, Context context) {
        if (components == null) {
            return;
        }

        Map<String, SecurityScheme> securitySchemes = components.getSecuritySchemes();
        if (securitySchemes == null) {
            return;
        }

        Iterator<Entry<String, SecurityScheme>> it = securitySchemes.entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, SecurityScheme> entry = it.next();
            SecurityScheme securityScheme = applyFilters(entry.getValue(), filters, context);
            if (securityScheme == null) {
                it.remove();
            } else if (securityScheme != entry.getValue()) {
                entry.setValue(securityScheme);
            }
        }
    }

    private SecurityScheme applyFilters(SecurityScheme securityScheme, OpenAPIFilter[] filters, Context context) {
        SecurityScheme result = securityScheme;
        for (OpenAPIFilter filter : filters) {
            result = filter.filterSecurityScheme(result, context);
            if (result == null) {
                return null;
            }
        }
        return result;
    }
}
