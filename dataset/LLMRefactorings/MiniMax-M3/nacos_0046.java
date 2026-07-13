public class nacos_0046 {

    static String updateFrontmatterField(String md, String field, String value) {
        if (StringUtils.isEmpty(md)) {
            return buildFrontmatter(field, value);
        }
        java.util.regex.Matcher matcher = FRONTMATTER_PATTERN.matcher(md);
        if (!matcher.find()) {
            return buildFrontmatter(field, value) + "\n" + md;
        }
        String frontmatterBlock = matcher.group(1);
        String[] lines = frontmatterBlock.split("\\r?\\n");
        boolean found = false;
        StringBuilder sb = new StringBuilder();
        for (String line : lines) {
            int colonIdx = line.indexOf(':');
            if (colonIdx > 0 && line.substring(0, colonIdx).trim().equals(field)) {
                int indentLen = 0;
                while (indentLen < line.length() && Character.isWhitespace(
                    line.charAt(indentLen))) {
                    indentLen++;
                }
                sb.append(line, 0, indentLen).append(field).append(": ").append(value);
                found = true;
            } else {
                sb.append(line);
            }
            sb.append('\n');
        }
        if (!found) {
            // Insert at the beginning of frontmatter so name always appears first
            sb.insert(0, field + ": " + value + "\n");
        }
        // Remove trailing newline before closing ---
        String updatedBlock = sb.toString();
        if (updatedBlock.endsWith("\n")) {
            updatedBlock = updatedBlock.substring(0, updatedBlock.length() - 1);
        }
        return md.substring(0, matcher.start()) + "---\n" + updatedBlock + "\n---"
            + md.substring(matcher.end());
    }

    private static String buildFrontmatter(String field, String value) {
        return "---\n" + field + ": " + value + "\n---\n";
    }
}
