public class nacos_0194 {

        @Override
        public List<ParamInfo> extractParamRefactored(HttpServletRequest request) throws NacosException {
            ParamInfo paramInfo = new ParamInfo();
            
            // Try to extract skill name from request body for optimization requests
            if (HTTP_METHOD_POST.equalsIgnoreCase(request.getMethod())) {
                try {
                    StringBuilder body = new StringBuilder();
                    try (BufferedReader reader = request.getReader()) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            body.append(line);
                        }
                    }
                    
                    if (body.length() > 0) {
                        // Parse JSON body to extract skill name
                        String bodyStr = body.toString();
                        if (bodyStr.contains(SKILL_JSON_KEY)) {
                            // Extract skill from request body
                            try {
                                java.util.Map<String, Object> bodyMap =
                                    JacksonUtils.toObj(bodyStr, java.util.Map.class);
                                java.util.Map<String, Object> skillMap =
                                    (java.util.Map<String, Object>) bodyMap.get("skill");
                                if (skillMap != null) {
                                    Skill skill =
                                        JacksonUtils.toObj(JacksonUtils.toJson(skillMap), Skill.class);
                                    if (skill != null && StringUtils.isNotBlank(skill.getName())) {
                                        paramInfo.setAgentName(skill.getName());
                                        paramInfo.setNamespaceId(skill.getNamespaceId());
                                    }
                                }
                            } catch (Exception e) {
                                // Ignore parsing errors
                            }
                        }
                    }
                } catch (Exception e) {
                    // Ignore errors
                }
            }
            
            // Fallback to query parameters
            if (StringUtils.isBlank(paramInfo.getAgentName())) {
                paramInfo.setAgentName(request.getParameter("skillName"));
            }
            if (StringUtils.isBlank(paramInfo.getNamespaceId())) {
                paramInfo.setNamespaceId(request.getParameter("namespaceId"));
            }
            
            return Collections.singletonList(paramInfo);
        }
}
