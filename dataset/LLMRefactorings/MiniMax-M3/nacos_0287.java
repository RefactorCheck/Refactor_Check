public class nacos_0287 {

        public Method getMethod(HttpServletRequest request) {
            String path = getPath(request);
            String httpMethod = request.getMethod();
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
            RequestMappingInfo bestMatch = findBestMatch(matchedInfo, request);
            return methods.get(bestMatch);
        }

        private RequestMappingInfo findBestMatch(List<RequestMappingInfo> matchedInfo, HttpServletRequest request) {
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
            return bestMatch;
        }
}
