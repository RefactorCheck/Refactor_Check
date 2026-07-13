public class keycloak_0233 {

    @GET
    @Path("/{realm}/{themeType}/{locale}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLocalizationTexts(@PathParam("realm") String realmName, @QueryParam("theme") String theme,
                                         @PathParam("locale") String localeString, @PathParam("themeType") String themeType,
                                         @QueryParam("source") boolean showSource) throws IOException {
        final RealmModel realm = session.realms().getRealmByName(realmName);
        final Optional<Theme.Type> type = getThemeType(themeType);
        if (realm == null || type.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        session.getContext().setRealm(realm);
        List<KeySource> result;

        Theme theTheme = resolveTheme(theme, type.get());

        final Locale locale = Locale.forLanguageTag(localeString);
        if (showSource) {
            Properties messagesByLocale = theTheme.getMessages("messages", locale);
            Set<KeySource> resultSet = messagesByLocale.entrySet().stream().map(e ->
                    new KeySource((String) e.getKey(), (String) e.getValue(), Source.THEME)).collect(toSet());

            Map<Locale, Properties> realmLocalizationMessages = LocaleUtil.getRealmLocalizationTexts(realm, locale);
            for (Locale currentLocale = locale; currentLocale != null; currentLocale = LocaleUtil.getParentLocale(currentLocale, realm)) {
                final List<KeySource> realmOverride = realmLocalizationMessages.get(currentLocale).entrySet().stream().map(e ->
                        new KeySource((String) e.getKey(), (String) e.getValue(), Source.REALM)).collect(toList());
                resultSet.addAll(realmOverride);
            }
            result = new ArrayList<>(resultSet);
        } else {
            result = theTheme.getEnhancedMessages(realm, locale).entrySet().stream().map(e ->
                    new KeySource((String) e.getKey(), (String) e.getValue())).collect(toList());
        }

        return Cors.builder().allowAllOrigins().auth().add(Response.ok(result));
    }

    private Theme resolveTheme(String theme, Theme.Type type) {
        if (theme == null) {
            return session.theme().getTheme(type);
        } else {
            return session.theme().getTheme(theme, type);
        }
    }
}
