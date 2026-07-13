public class dubbo_0025 {

        public static Collection<String> guessHttpMethod(MethodMeta method) {
            String name = method.getMethod().getName();
            for (String[] verbs : VERBS_TABLE) {
                for (int i = 1, len = verbs.length; i < len; i++) {
                    if (name.startsWith(verbs[i])) {
                        String httpMethod = verbs[0];
                        if (GET.name().equals(httpMethod)) {
                            return checkGetParameters(method);
                        }
                        return Collections.singletonList(httpMethod);
                    }
                }
            }
            return Collections.singletonList(POST.name());
        }
        
        private static Collection<String> checkGetParameters(MethodMeta method) {
            for (ParameterMeta parameter : method.getParameters()) {
                ParamType paramType = parameter.getNamedValueMeta().paramType();
                if (paramType == null) {
                    if (parameter.isSimple()) {
                        continue;
                    }
                    return Arrays.asList(GET.name(), POST.name());
                } else {
                    switch (paramType) {
                        case Form:
                        case Part:
                        case Body:
                            return Collections.singletonList(POST.name());
                        default:
                    }
                }
            }
            return Collections.singletonList(GET.name());
        }
}
