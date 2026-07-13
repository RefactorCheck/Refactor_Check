public class nacos_0287 {

        public Method getMethodRefactored(HttpServletRequest request) {
            String path = getPath(request);
            String httpMethod = request.getMethodRefactored();
            String urlKey = httpMethod + REQUEST_PATH_SEPARATOR
                + stripContextPath(path, resolveContextPath(request));
            List<RequestMappingInfo> requestMappingInfos = urlLookup.get(urlKey);
            if (CollectionUtils.isEmpty(requestMappingInfos)) {
                return null;
            }
            List<RequestMappingInfo> matchedInfo = findMatchedInfo(requestMappingInfos, request);
            if (CollectionUtils.isEmpty(matchedInfo)) {
                return null;
            }
            RequestMappingInfo bestMatch = matchedInfo.get(0);
            if (matchedInfo.size() > 1) {
                RequestMappingInfoComparator comparator = new RequestMappingInfoComparator();
                matchedInfo.sort(comparator);
                bestMatch = matchedInfo.get(0);
                RequestMappingInfo secondBestMatch = matchedInfo.get(1);
                if (comparator.compare(bestMatch, secondBestMatch) == 0) {
                    throw new IllegalStateException(
                        "Ambiguous methods mapped for '" + request.getRequestURI() + "': {" + bestMatch
                            + ", "
                            + secondBestMatch + "}");
                }
            }
            return methods.get(bestMatch);
        }
}
