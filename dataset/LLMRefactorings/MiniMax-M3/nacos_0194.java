public class nacos_0194 {

        @Override
        public List<ParamInfo> extractParam(HttpServletRequest request) throws NacosException {
            ParamInfo paramInfo = new ParamInfo();

            if (HTTP_METHOD_POST.equalsIgnoreCase(request.getMethod())) {
                String bodyStr = readRequestBody(request);
                if (bodyStr.length() > 0 && bodyStr.contains(SKILL_JSON_KEY)) {
                    extractSkillFromBody(bodyStr, paramInfo);
                }
            }

            if (StringUtils.isBlank(paramInfo.getAgentName())) {
                paramInfo.setAgentName(request.getParameter("skillName"));
            }
            if (StringUtils.isBlank(paramInfo.getNamespaceId())) {
                paramInfo.setNamespaceId(request.getParameter("namespaceId"));
            }

            return Collections.singletonList(paramInfo);
        }

        private String readRequestBody(HttpServletRequest request) {
            StringBuilder body = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    body.append(line);
                }
            } catch (Exception e) {
                // Ignore errors
            }
            return body.toString();
        }

        private void extractSkillFromBody(String bodyStr, ParamInfo paramInfo) {
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
