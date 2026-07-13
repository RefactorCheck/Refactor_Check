public class dubbo_0272 {

        @Override
        public void create(final String path, boolean ephemeral, boolean faultTolerant) {
            if (!ephemeral) {
                if (persistentExistNodePath.contains(path)) {
                    return;
                }
                if (checkExists(path)) {
                    persistentExistNodePath.add(path);
                    return;
                }
            }
            int i = path.lastIndexOf('/');
            if (i > 0) {
                create(path.substring(0, i), false, true);
            }
            if (ephemeral) {
                createEphemeral(path, faultTolerant);
            } else {
                createPersistent(path, faultTolerant);
                persistentExistNodePath.add(path);
            }
        }
}
