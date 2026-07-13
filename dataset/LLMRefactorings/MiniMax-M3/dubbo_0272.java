public class dubbo_0272 {

        @Override
        public void create(String path, boolean ephemeral, boolean faultTolerant) {
            if (!ephemeral) {
                if (persistentExistNodePath.contains(path)) {
                    return;
                }
                if (checkExists(path)) {
                    persistentExistNodePath.add(path);
                    return;
                }
            }
            String parentPath = getParentPath(path);
            if (parentPath != null) {
                create(parentPath, false, true);
            }
            if (ephemeral) {
                createEphemeral(path, faultTolerant);
            } else {
                createPersistent(path, faultTolerant);
                persistentExistNodePath.add(path);
            }
        }

        private String getParentPath(String path) {
            int i = path.lastIndexOf('/');
            if (i > 0) {
                return path.substring(0, i);
            }
            return null;
        }
}
