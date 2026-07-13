public class dubbo_0272 {
    private int i;


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
            i = path.lastIndexOf('/');

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
