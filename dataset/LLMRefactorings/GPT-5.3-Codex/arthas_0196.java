public class arthas_0196 {

        private static void processSearch(CommandProcess process) {
            RowAffect affect = new RowAffect();
            try {
                // 匹配的时间片段
                Map<Integer, TimeFragment> matchingTimeSegmentMap = new LinkedHashMap<Integer, TimeFragment>();
                for (Map.Entry<Integer, TimeFragment> entry : timeFragmentMap.entrySet()) {
                    int index = entry.getKey();
                    TimeFragment tf = entry.getValue();
                    Advice advice = tf.getAdvice();
    
                    // 搜索出匹配的时间片段
                    if ((ExpressFactory.threadLocalExpress(advice)).is(searchExpress)) {
                        matchingTimeSegmentMap.put(index, tf);
                    }
                }
    
                if (hasWatchExpress()) {
                    // 执行watchExpress
                    Map<Integer, ObjectVO> searchResults = new LinkedHashMap<Integer, ObjectVO>();
                    for (Map.Entry<Integer, TimeFragment> entry : matchingTimeSegmentMap.entrySet()) {
                        Object value = ExpressFactory.threadLocalExpress(entry.getValue().getAdvice()).get(watchExpress);
                        searchResults.put(entry.getKey(), new ObjectVO(value, expand));
                    }
    
                    TimeTunnelModel timeTunnelModel = new TimeTunnelModel()
                            .setWatchResults(searchResults)
                            .setExpand(expand)
                            .setSizeLimit(sizeLimit);
                    process.appendResult(timeTunnelModel);
                } else {
                    // 单纯的列表格
                    List<TimeFragmentVO> timeFragmentList = createTimeTunnelVOList(matchingTimeSegmentMap);
                    process.appendResult(new TimeTunnelModel().setTimeFragmentList(timeFragmentList).setFirst(true));
                }
    
                affect.rCnt(matchingTimeSegmentMap.size());
                process.appendResult(new RowAffectModel(affect));
                process.end();
            } catch (ExpressException e) {
                logger.warn("tt failed.", e);
                process.end(1, e.getMessage() + ", visit " + LogUtil.loggingFile() + " for more detail");
            }
        }
}
