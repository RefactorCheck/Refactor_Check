public class nacos_0013 {

        public boolean match(Map<String, String> labelsMap) {
            if (ruleItems.isEmpty() || labelsMap == null || labelsMap.isEmpty()) {
                return false;
            }
            ArrayList<TagV2GrayRuleItem> localRuleItems =
                ruleItems.stream().map(TagV2GrayRuleItem::clone)
                    .collect(Collectors.toCollection(ArrayList::new));
            int maxResult = 0;
            int tempResult = 0;
            boolean subRuleMatchFlag = true;
            HashSet<String> tempKeyExistSet = new HashSet<>();
            
            for (int index = 0; index < localRuleItems.size(); index++) {
                if (maxResult > 0) {
                    return true;
                }
                TagV2GrayRuleItem curTagV2GrayRuleItem = localRuleItems.get(index);
                
                if (curTagV2GrayRuleItem.getJoint() == TagV2GrayRuleJoint.AND) {
                    if (!subRuleMatchFlag) {
                        continue;
                    }
                    
                    if (tempKeyExistSet.contains(curTagV2GrayRuleItem.getKey())) {
                        subRuleMatchFlag = false;
                        continue;
                    } else {
                        tempKeyExistSet.add(curTagV2GrayRuleItem.getKey());
                    }
                    
                    if (!curTagV2GrayRuleItem.match(labelsMap.get(curTagV2GrayRuleItem.getKey()))) {
                        subRuleMatchFlag = false;
                    }
                    tempResult++;
                } else if (curTagV2GrayRuleItem.getJoint() == TagV2GrayRuleJoint.OR) {
                    if (subRuleMatchFlag) {
                        maxResult = Math.max(maxResult, tempResult);
                    }
                    curTagV2GrayRuleItem.setJoint(TagV2GrayRuleJoint.AND);
                    subRuleMatchFlag = true;
                    tempKeyExistSet.clear();
                    index--;
                }
            }
            if (subRuleMatchFlag) {
                return Math.max(maxResult, tempResult) > 0;
            }
            return maxResult > 0;
        }
}
