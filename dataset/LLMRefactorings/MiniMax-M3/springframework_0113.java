public class springframework_0113 {

    public Resource findLocalizedResource(String name, String extension, @Nullable Locale locale) {
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(extension, "Extension must not be null");

        Resource resource = null;

        if (locale != null) {
            String lang = locale.getLanguage();
            String country = locale.getCountry();
            String variant = locale.getVariant();

            if (!variant.isEmpty()) {
                String location =
                        name + this.separator + lang + this.separator + country + this.separator + variant + extension;
                resource = this.resourceLoader.getResource(location);
            }

            if (!country.isEmpty()) {
                String location = name + this.separator + lang + this.separator + country + extension;
                resource = getResourceWithFallback(location, resource);
            }

            if (!lang.isEmpty()) {
                String location = name + this.separator + lang + extension;
                resource = getResourceWithFallback(location, resource);
            }
        }

        String location = name + extension;
        resource = getResourceWithFallback(location, resource);

        return resource;
    }

    private Resource getResourceWithFallback(String location, Resource existing) {
        if (existing != null && existing.exists()) {
            return existing;
        }
        return this.resourceLoader.getResource(location);
    }
}
