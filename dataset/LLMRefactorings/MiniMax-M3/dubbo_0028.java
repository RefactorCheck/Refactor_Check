public class dubbo_0028 {

        private void refreshArgument(ArgumentConfig argument, InmemoryConfiguration subPropsConfiguration) {
            if (argument.getIndex() != null && argument.getIndex() >= 0) {
                String prefix = argument.getIndex() + ".";
                Environment environment = getScopeModel().modelEnvironment();
                List<java.lang.reflect.Method> methods =
                        MethodUtils.getMethods(argument.getClass(), method -> method.getDeclaringClass() != Object.class);
                for (java.lang.reflect.Method method : methods) {
                    if (MethodUtils.isSetter(method)) {
                        String propertyName = extractPropertyName(method.getName());
                        if (StringUtils.isEquals(propertyName, "index") || StringUtils.isEquals(propertyName, "type")) {
                            continue;
                        }
                        String kebabPropertyName = prefix + StringUtils.convertToSplitName(propertyName, "-");
                        applyProperty(argument, subPropsConfiguration, method, kebabPropertyName, environment);
                    }
                }
            }
        }

        private void applyProperty(ArgumentConfig argument, InmemoryConfiguration subPropsConfiguration,
                                   java.lang.reflect.Method method, String kebabPropertyName, Environment environment) {
            try {
                String value = StringUtils.trim(subPropsConfiguration.getString(kebabPropertyName));
                if (StringUtils.hasText(value)
                        && ClassUtils.isTypeMatch(method.getParameterTypes()[0], value)) {
                    value = environment.resolvePlaceholders(value);
                    method.invoke(
                            argument,
                            ClassUtils.convertPrimitive(
                                    ScopeModelUtil.getFrameworkModel(getScopeModel()),
                                    method.getParameterTypes()[0],
                                    value));
                }
            } catch (Exception e) {
                logger.info("Failed to override the property " + method.getName() + " in "
                        + this.getClass().getSimpleName()
                        + ", please make sure every property has getter/setter method provided.");
            }
        }
}
