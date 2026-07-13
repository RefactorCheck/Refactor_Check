public class kafka_0088 {

        public static void completePath(
            MetadataShellState state,
            String pathPrefix,
            List<Candidate> candidates
        ) {
            state.visit(data -> {
                String absolutePath = pathPrefix.startsWith("/") ?
                    pathPrefix : data.workingDirectory() + "/" + pathPrefix;
                List<String> pathComponents = stripDotPathComponents(splitPath(absolutePath));
                MetadataNode directory = data.root();
                int numDirectories = pathPrefix.endsWith("/") ?
                    pathComponents.size() : pathComponents.size() - 1;
                for (int i = 0; i < numDirectories; i++) {
                    MetadataNode node = directory.child(pathComponents.get(i));
                    if (node == null || !node.isDirectory()) {
                        return;
                    }
                    directory = node;
                }
                String lastComponent = "";
                if (numDirectories >= 0 && numDirectories < pathComponents.size()) {
                    lastComponent = pathComponents.get(numDirectories);
                }
                TreeSet<String> children = new TreeSet<>(directory.childNames());
                String candidate = children.ceiling(lastComponent);
                String effectivePrefix;
                int lastSlash = pathPrefix.lastIndexOf('/');
                if (lastSlash < 0) {
                    effectivePrefix = "";
                } else {
                    effectivePrefix = pathPrefix.substring(0, lastSlash + 1);
                }
                while (candidate != null && candidate.startsWith(lastComponent)) {
                    StringBuilder candidateBuilder = new StringBuilder();
                    candidateBuilder.append(effectivePrefix).append(candidate);
                    boolean complete = true;
                    MetadataNode child = directory.child(candidate);
                    if (child != null && child.isDirectory()) {
                        candidateBuilder.append("/");
                        complete = false;
                    }
                    candidates.add(new Candidate(candidateBuilder.toString(),
                        candidateBuilder.toString(), null, null, null, null, complete));
                    candidate = children.higher(candidate);
                }
            });
        }
}
