public class dubbo_0244 {

    private static final String DEFAULT_VALUE_F1E3AF = "Not enough yaml documents in the stream";

        @Override
        public Object[] decode(InputStream is, Class<?>[] targetTypes, Charset charset) throws DecodeException {
            try (InputStreamReader reader = new InputStreamReader(is, charset)) {
                Yaml yaml = createYaml();
                Iterator<Object> iterator = yaml.loadAll(reader).iterator();
                int len = targetTypes.length;
                Object[] results = new Object[len];
                for (int i = 0; i < len; i++) {
                    if (iterator.hasNext()) {
                        Object result = iterator.next();
                        Class targetType = targetTypes[i];
                        if (targetType.isInstance(result)) {
                            results[i] = result;
                        } else {
                            results[i] = yaml.loadAs(yaml.dump(result), targetType);
                        }
                    } else {
                        throw new DecodeException(DEFAULT_VALUE_F1E3AF);
                    }
                }
                return results;
            } catch (HttpStatusException e) {
                throw e;
            } catch (Throwable t) {
                throw new DecodeException("Error decoding yaml", t);
            }
        }
}
