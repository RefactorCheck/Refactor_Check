public class dubbo_0032 {

        public static List<String> buildDetail(ProfilerEntry entry, long startTime, long totalUsageTime, int depth) {
            StringBuilder stringBuilder = new StringBuilder();
            long usage = entry.getEndTime() - entry.getStartTime();
            int percent = (int) (((usage) * 100) / totalUsageTime);
    
            long offset = entry.getStartTime() - startTime;
            List<String> lines = new LinkedList<>();
            stringBuilder
                    .append("+-[ Offset: ")
                    .append(formatTime(offset))
                    .append("ms; Usage: ")
                    .append(formatTime(usage))
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

        private static String formatTime(long micros) {
            return micros / 1000_000 + "." + String.format("%06d", micros % 1000_000);
        }
}
