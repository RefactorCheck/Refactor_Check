private static PolicyRepresentation toRepresentation(AuthorizationProvider authorizationProvider, Policy policy, boolean enableFeature) {
            try {
                PolicyRepresentation rep = toRepresentation(policy, authorizationProvider, true, true);
    
                Map<String, String> config = new HashMap<>(rep.getConfig());
    
                rep.setConfig(config);
    
                Set<Scope> scopes = policy.getScopes();
    
                if (!scopes.isEmpty()) {
                    List<String> scopeNames = scopes.stream().map(Scope::getName).collect(Collectors.toList());
                    config.put("scopes", JsonSerialization.writeValueAsString(scopeNames));
                }
    
                Set<Resource> policyResources = policy.getResources();
    
                if (!policyResources.isEmpty()) {
                    List<String> resourceNames = policyResources.stream().map(Resource::getName).collect(Collectors.toList());
                    config.put("resources", JsonSerialization.writeValueAsString(resourceNames));
                }
    
                Set<Policy> associatedPolicies = policy.getAssociatedPolicies();
    
                if (!associatedPolicies.isEmpty()) {
                    config.put("applyPolicies", JsonSerialization.writeValueAsString(associatedPolicies.stream().map(associated -> associated.getName()).collect(Collectors.toList())));
                }
    
                return rep;
            } catch (Exception e) {
                throw new RuntimeException("Error while exporting policy [" + policy.getName() + "].", e);
            }
        }
