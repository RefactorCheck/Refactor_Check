public class dubbo_0032 {

        public static List<String> buildDetail(ProfilerEntry entry, long startTime, long totalUsageTime, int depth) {
            StringBuilder stringBuilder = new StringBuilder();
            long usage = entry.getEndTime() - entry.getStartTime();
            int percent = (int) (((usage) * 100) / totalUsageTime);
    
            long offset = entry.getStartTime() - startTime;
            List<String> lines = new LinkedList<>();
            stringBuilder
                    .append("+-[ Offset: ")
                    .append(offset / 1000_000)
                    .append('.')
                    .append(String.format("%06d", offset % 1000_000))
                    .append("ms; Usage: ")
                    .append(usage / 1000_000)
                    .append('.')
                    .append(String.format("%06d", usage % 1000_000))
                    .append("ms, ")
                    .append(percent)
                    .append("% ] ")
                    .append(entry.getMessage());
            lines.add(stringBuilder.toString());
            List<ProfilerEntry> entrySub = entry.getSub();
            for (int i = 0, entrySubSize = entrySub.size(); i < entrySubSize; i++) {
                ProfilerEntry sub = entrySub.get(i);
                List<String> subLines = buildDetail(sub, startTime, totalUsageTime, depth + 1);
                if (i < entrySubSize - 1) {
                    lines.add("  " + subLines.get(0));
                    for (int j = 1, subLinesSize = subLines.size(); j < subLinesSize; j++) {
                        String subLine = subLines.get(j);
                        lines.add("  |" + subLine);
                    }
                } else {
                    lines.add("  " + subLines.get(0));
                    for (int j = 1, subLinesSize = subLines.size(); j < subLinesSize; j++) {
                        String subLine = subLines.get(j);
                        lines.add("   " + subLine);
                    }
                }
            }
            return lines;
        }
}
