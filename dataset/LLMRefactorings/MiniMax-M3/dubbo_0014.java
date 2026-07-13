public class dubbo_0014 {

        private void mergePath(String path, PathItem target, PathItem source, String group, String version, String[] tags) {
            mergeBasicProperties(target, source);
            mergeOperations(path, target, source, group, version, tags);
            mergeServers(target, source);
            mergeParameters(target, source);
            target.addExtensions(source.getExtensions());
        }

        private void mergeBasicProperties(PathItem target, PathItem source) {
            if (target.getRef() == null) {
                target.setRef(source.getRef());
            }
            if (target.getSummary() == null) {
                target.setSummary(source.getSummary());
            }
            if (target.getDescription() == null) {
                target.setDescription(source.getDescription());
            }
        }

        private void mergeOperations(String path, PathItem target, PathItem source, String group, String version, String[] tags) {
            Map<HttpMethods, Operation> sourceOperations = source.getOperations();
            if (sourceOperations != null) {
                for (Entry<HttpMethods, Operation> entry : sourceOperations.entrySet()) {
                    HttpMethods httpMethod = entry.getKey();
                    Operation sourceOperation = entry.getValue();
                    if (isGroupNotMatch(group, sourceOperation.getGroup())
                            || isVersionNotMatch(version, sourceOperation.getVersion())
                            || isTagNotMatch(tags, sourceOperation.getTags())) {
                        continue;
                    }

                    Operation operation = target.getOperation(httpMethod);
                    if (operation == null) {
                        target.addOperation(httpMethod, sourceOperation.clone());
                    } else if (operation.getMeta() != null) {
                        LOG.internalWarn(
                                "Operation already exists, path='{}', httpMethod='{}', method={}",
                                path,
                                httpMethod,
                                sourceOperation.getMeta());
                    }
                }
            }
        }

        private void mergeServers(PathItem target, PathItem source) {
            if (target.getServers() == null) {
                List<Server> sourceServers = source.getServers();
                if (sourceServers != null) {
                    target.setServers(Node.clone(sourceServers));
                }
            }
        }

        private void mergeParameters(PathItem target, PathItem source) {
            List<Parameter> sourceParameters = source.getParameters();
            if (sourceParameters != null) {
                if (target.getParameters() == null) {
                    target.setParameters(Node.clone(sourceParameters));
                } else {
                    for (Parameter parameter : sourceParameters) {
                        target.addParameter(parameter.clone());
                    }
                }
            }
        }
}
