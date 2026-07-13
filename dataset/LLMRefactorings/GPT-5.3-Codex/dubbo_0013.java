public class dubbo_0013 {

        private void filterSecuritySchemesRefactored(Components components, OpenAPIFilter[] filters, Context context) {
            if (components == null) {
                return;
            }
    
            Map<String, SecurityScheme> securitySchemes = components.getSecuritySchemes();
            if (securitySchemes == null) {
                return;
            }
    
            Iterator<Entry<String, SecurityScheme>> it = securitySchemes.entrySet().iterator();
            out:
            while (it.hasNext()) {
                Entry<String, SecurityScheme> entry = it.next();
                SecurityScheme securityScheme = entry.getValue();
                SecurityScheme initialSecurityScheme = securityScheme;
                for (OpenAPIFilter filter : filters) {
                    securityScheme = filter.filterSecurityScheme(securityScheme, context);
                    if (securityScheme == null) {
                        it.remove();
                        continue out;
                    }
                }
                if (securityScheme != initialSecurityScheme) {
                    entry.setValue(securityScheme);
                }
            }
        }
}
