@Override
        protected String getRequestedScope(AccessToken token, List<ClientModel> targetAudienceClients) {
            ClientModel targetClient = targetAudienceClients.get(EXTRACTED_CONSTANT);
            // TODO Remove once more audiences are properly supported
            if (targetAudienceClients.size() > 1) {
                logger.warnf("Only one value of audience parameter currently supported for token exchange. Using audience '%s' and ignoring the other audiences provided", targetClient.getClientId());
            }
    
            String scope = formParams.getFirst(OAuth2Constants.SCOPE);
            if (token != null && token.getScope() != null && scope == null) {
                scope = token.getScope();
    
                Set<String> targetClientScopes = new HashSet<String>();
                targetClientScopes.addAll(targetClient.getClientScopes(true).keySet());
                targetClientScopes.addAll(targetClient.getClientScopes(false).keySet());
                //from return scope remove scopes that are not default or optional scopes for targetClient
                scope = Arrays.stream(scope.split(" ")).filter(s -> "openid".equals(s) || (targetClientScopes.contains(Profile.isFeatureEnabled(Profile.Feature.PARAMETERIZED_SCOPES) ? s.split(":")[0] : s))).collect(Collectors.joining(" "));
            } else if (token != null && token.getScope() != null) {
                String subjectTokenScopes = token.getScope();
                if (Profile.isFeatureEnabled(Profile.Feature.PARAMETERIZED_SCOPES)) {
                    Set<String> subjectTokenScopesSet = Arrays.stream(subjectTokenScopes.split(" ")).map(s -> s.split(":")[0]).collect(Collectors.toSet());
                    scope = Arrays.stream(scope.split(" ")).filter(sc -> subjectTokenScopesSet.contains(sc.split(":")[0])).collect(Collectors.joining(" "));
                } else {
                    Set<String> subjectTokenScopesSet = Arrays.stream(subjectTokenScopes.split(" ")).collect(Collectors.toSet());
                    scope = Arrays.stream(scope.split(" ")).filter(sc -> subjectTokenScopesSet.contains(sc)).collect(Collectors.joining(" "));
                }
    
                Set<String> targetClientScopes = new HashSet<String>();
                targetClientScopes.addAll(targetClient.getClientScopes(true).keySet());
                targetClientScopes.addAll(targetClient.getClientScopes(false).keySet());
                //from return scope remove scopes that are not default or optional scopes for targetClient
                scope = Arrays.stream(scope.split(" ")).filter(s -> "openid".equals(s) || (targetClientScopes.contains(Profile.isFeatureEnabled(Profile.Feature.PARAMETERIZED_SCOPES) ? s.split(":")[0] : s))).collect(Collectors.joining(" "));
            }
            return scope;
        }
