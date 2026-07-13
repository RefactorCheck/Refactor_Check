public class arthas_0034 {

        private static CollapsedProfile parseCollapsed(String collapsed, boolean threadsEnabled) {
            CollapsedProfile profile = new CollapsedProfile();
    
            String[] lines = collapsed.split("\\r?\\n");
            for (String line : lines) {
                parseLine(line, profile, threadsEnabled);
            }
    
            return profile;
        }

        private static void parseLine(String line, CollapsedProfile profile, boolean threadsEnabled) {
            if (line == null) {
                return;
            }
            line = line.trim();
            if (line.isEmpty()) {
                return;
            }
    
            // collapsed 格式：frame1;frame2;frame3 <samples>
            int spaceIdx = lastSpaceIndex(line);
            if (spaceIdx < 0) {
                return;
            }
            String stack = line.substring(0, spaceIdx).trim();
            String countStr = line.substring(spaceIdx + 1).trim();
            if (stack.isEmpty() || countStr.isEmpty()) {
                return;
            }
    
            long samples;
            try {
                samples = Long.parseLong(countStr);
            } catch (Throwable ignore) {
                return;
            }
            if (samples <= 0) {
                return;
            }
    
            // threads 模式下，栈最后可能会附带线程名帧；为了减少噪音，这里尽量去掉它。
            String normalizedStack = stripThreadFrame(stack, threadsEnabled);
            profile.totalSamples += samples;
            profile.stacks.add(new StackSample(normalizedStack, samples));
    
            addToCallTree(profile.callTreeRoot, normalizedStack, samples);
    
            String topFrame = topFrame(normalizedStack, threadsEnabled);
            if (topFrame == null || topFrame.isEmpty()) {
                return;
            }
            Long old = profile.selfSamples.get(topFrame);
            profile.selfSamples.put(topFrame, old == null ? samples : old + samples);
    
            addTopStacksByLeaf(profile.topStacksByLeaf, topFrame, normalizedStack, samples, 3);
        }
}
