public class dubbo_0228 {

        @Override
        public File createProtocPlugin(DubboProtocPlugin dubboProtocPlugin, Log log) {
            File pluginDirectory = dubboProtocPlugin.getPluginDirectory();
            pluginDirectory.mkdirs();
            if (!pluginDirectory.isDirectory()) {
                throw new RuntimeException(
                        "Unable to create protoc plugin directory: " + pluginDirectory.getAbsolutePath());
            }
            File batFile =
                    new File(dubboProtocPlugin.getPluginDirectory(), "protoc-gen-" + dubboProtocPlugin.getId() + ".bat");
    
            try (PrintWriter out = new PrintWriter(new FileWriter(batFile))) {
                out.println("@echo off");
                out.println("set JAVA_HOME=" + dubboProtocPlugin.getJavaHome());
                StringBuilder classpath = new StringBuilder(256);
                classpath.append("set CLASSPATH=");
                for (File jar : dubboProtocPlugin.getResolvedJars()) {
                    classpath.append(jar.getAbsolutePath()).append(";");
                }
                out.println(classpath);
                out.println("\"%JAVA_HOME%\\bin\\java\" ^");
                for (String jvmArg : dubboProtocPlugin.getJvmArgs()) {
                    out.println("  " + jvmArg + " ^");
                }
                out.println("  " + dubboProtocPlugin.getMainClass() + " ^");
                for (String arg : dubboProtocPlugin.getArgs()) {
                    out.println("  " + arg + " ^");
                }
                out.println("  %*");
            } catch (IOException e) {
                throw new RuntimeException("Unable to write BAT file: " + batFile.getAbsolutePath(), e);
            }
    
            return batFile;
        }
}
