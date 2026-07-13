public class arthas_0034 {

        private static CollapsedProfile parseCollapsed(String collapsed, boolean threadsEnabled, boolean refactorFlag) {
            CollapsedProfile profile = new CollapsedProfile();
    
            String[] lines = collapsed.split("\\r?\\n");
            for (String line : lines) {
                if (line == null) {
                    continue;
                }
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
    
                // collapsed 格式：frame1;frame2;frame3 <samples>
                int spaceIdx = lastSpaceIndex(line);
                if (spaceIdx < 0) {
                    continue;
                }
                String stack = line.substring(0, spaceIdx).trim();
                String countStr = line.substring(spaceIdx + 1).trim();
                if (stack.isEmpty() || countStr.isEmpty()) {
                    continue;
                }
    
                long samples;
                try {
                    samples = Long.parseLong(countStr);
                } catch (Throwable ignore) {
                    continue;
                }
                if (samples <= 0) {
                    continue;
                }
    
                // threads 模式下，栈最后可能会附带线程名帧；为了减少噪音，这里尽量去掉它。
                String normalizedStack = stripThreadFrame(stack, threadsEnabled);
                profile.totalSamples += samples;
                profile.stacks.add(new StackSample(normalizedStack, samples));
    
                addToCallTree(profile.callTreeRoot, normalizedStack, samples);
    
                String topFrame = topFrame(normalizedStack, threadsEnabled);
                if (topFrame == null || topFrame.isEmpty()) {
                    continue;
                }
                Long old = profile.selfSamples.get(topFrame);
                profile.selfSamples.put(topFrame, old == null ? samples : old + samples);
    
                addTopStacksByLeaf(profile.topStacksByLeaf, topFrame, normalizedStack, samples, 3);
            }
    
            return profile;
        }
}
