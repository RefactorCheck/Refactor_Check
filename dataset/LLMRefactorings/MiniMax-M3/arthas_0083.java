public class arthas_0083 {

    @Override
    public void draw(CommandProcess process, ClassLoaderModel result) {
        if (result.getMatchedClassLoaders() != null) {
            drawMatchedClassLoaders(process, result);
            return;
        }
        drawNonMatchedResults(process, result);
    }

    private void drawMatchedClassLoaders(CommandProcess process, ClassLoaderModel result) {
        process.write("Matched classloaders: \n");
        drawClassLoaders(process, result.getMatchedClassLoaders(), false);
        process.write("\n");
    }

    private void drawNonMatchedResults(CommandProcess process, ClassLoaderModel result) {
        if (result.getClassSet() != null) {
            drawAllClasses(process, result.getClassSet());
        }
        if (result.getResources() != null) {
            drawResources(process, result.getResources());
        }
        if (result.getLoadClass() != null) {
            drawLoadClass(process, result.getLoadClass());
        }
        if (result.getUrls() != null) {
            drawClassLoaderUrls(process, result.getUrls());
        }
        if (result.getClassLoaders() != null) {
            drawClassLoaders(process, result.getClassLoaders(), result.getTree());
        }
        if (result.getClassLoaderStats() != null) {
            drawClassLoaderStats(process, result.getClassLoaderStats());
        }
        if (result.getUrlStats() != null) {
            drawUrlStats(process, result.getUrlStats());
        }
        if (result.getUrlClassStats() != null) {
            drawUrlClassStats(process, result.getClassLoader(), result.getUrlClassStats(),
                    Boolean.TRUE.equals(result.getUrlClassStatsDetail()));
        }
    }
}
