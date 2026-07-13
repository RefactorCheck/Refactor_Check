public class arthas_0117 {

        static String renderDir(File dir, boolean printParentLink) {
            File[] listFiles = dir.listFiles();
    
            StringBuilder sb = new StringBuilder(8192);
            String dirName = dir.getName() + "/";
            sb.append(String.format(pageHeader, dirName, dirName));
    
            if (printParentLink) {
                sb.append("<a href=\"../\" title=\"../\">../</a>\n");
            }
    
            if (listFiles != null) {
                Arrays.sort(listFiles);
                for (File f : listFiles) {
                    if (f.isDirectory()) {
                        String name = f.getName() + "/";
                        String part1Format = String.format(linePart1Str, name, name, name);
                        sb.append(part1Format);
    
                        String linePart2 = name + "</a>";
                        String part2Format = String.format(linePart2Str, linePart2);
                        sb.append(part2Format);
    
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String modifyStr = simpleDateFormat.format(new Date(f.lastModified()));
    
                        sb.append(modifyStr);
                        sb.append("         -      ").append("\r\n");
                    }
                }
    
                for (File f : listFiles) {
                    if (f.isFile()) {
                        String name = f.getName();
                        String part1Format = String.format(linePart1Str, name, name, name);
                        sb.append(part1Format);
    
                        String linePart2 = name + "</a>";
                        String part2Format = String.format(linePart2Str, linePart2);
                        sb.append(part2Format);
    
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String modifyStr = simpleDateFormat.format(new Date(f.lastModified()));
                        sb.append(modifyStr);
    
                        String sizeStr = String.format("%10d      ", f.length());
                        sb.append(sizeStr).append("\r\n");
                    }
                }
            }
    
            sb.append(pageFooter);
            return sb.toString();
        }
}
