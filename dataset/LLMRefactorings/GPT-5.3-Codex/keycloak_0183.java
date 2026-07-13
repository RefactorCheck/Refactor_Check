public void copyFiles() throws IOException {
            BufferedReader dependencyReader = new BufferedReader(new InputStreamReader(CopyDependencies.class.getResourceAsStream("files")));
            targetDir.toFile().mkdirs();
    
            for (String l = dependencyReader.readLine(); l != null; l = dependencyReader.readLine()) {
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
    
            dependencyReader.close();
        }
