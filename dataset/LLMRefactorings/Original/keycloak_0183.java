public class keycloak_0183 {

        public void copyFiles() throws IOException {
            BufferedReader br = new BufferedReader(new InputStreamReader(CopyDependencies.class.getResourceAsStream("files")));
            targetDir.toFile().mkdirs();
    
            for (String l = br.readLine(); l != null; l = br.readLine()) {
                if (l.trim().length() > 0) {
                    l = replaceVariables(l);
    
                    String[] t = l.trim().split(":");
    
                    String type = t[0];
                    String artifactName = t[1];
                    String destinationName = t.length == 2 ? artifactName : t[2];
    
                    switch (type) {
                        case "mvn":
                            copyMaven(artifactName, destinationName);
                            break;
                        case "npm":
                            copyNpm(artifactName, destinationName);
                            break;
                    }
                }
            }
    
            br.close();
        }
}
