public class nacos_0191 {

        private static List<SkillResource> sortResources(Map<String, SkillResource> resources) {
            List<SkillResource> sorted = new ArrayList<>(resources.values());
            sorted.removeIf(r -> r == null || StringUtils.isBlank(r.getName()));
            Collections.sort(sorted, (a, b) -> {
                String ka = safeIdentifier(a);
                String kb = safeIdentifier(b);
                return ka.compareTo(kb);
            });
            return sorted;
        }

        public static String computeContentMd5(Skill skill) {
            if (skill == null) {
                throw new IllegalArgumentException("Skill cannot be null when computing content MD5");
            }
            try {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                String skillMd = skill.getSkillMd();
                if (skillMd != null) {
                    buffer.write(skillMd.getBytes(StandardCharsets.UTF_8));
                }
                buffer.write(SEPARATOR);

                Map<String, SkillResource> resources = skill.getResource();
                if (resources != null && !resources.isEmpty()) {
                    List<SkillResource> sorted = sortResources(resources);
                    for (SkillResource resource : sorted) {
                        buffer.write(safeIdentifier(resource).getBytes(StandardCharsets.UTF_8));
                        buffer.write(SEPARATOR);
                        String content = resource.getContent();
                        if (content != null) {
                            buffer.write(content.getBytes(StandardCharsets.UTF_8));
                        }
                        buffer.write(SEPARATOR);
                    }
                }
                return MD5Utils.md5Hex(buffer.toByteArray());
            } catch (IOException e) {
                // ByteArrayOutputStream never throws; keep checked-style for completeness.
                throw new IllegalStateException("Failed to assemble skill content for MD5", e);
            } catch (NoSuchAlgorithmException e) {
                throw new IllegalStateException("MD5 algorithm not available in current JVM", e);
            }
        }
}
