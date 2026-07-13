public static FetchOnServerWrapper<Integer> getClientSessionsCountInUserSession(String sessionId) {
            return new FetchOnServerWrapper<>() {
                @Override
                public FetchOnServer getRunOnServer() {
                    return session -> {
                        RealmModel realm = session.getContext().getRealm();
    
                        UserSessionModel sessionModel = session.sessions().getUserSession(realm, sessionId);
                        if (sessionModel == null) {
                            throw new NotFoundException(EXTRACTED_CONSTANT);
                        }
    
                        // TODO: Might need optimization to prevent loading client sessions from cache
                        return sessionModel.getAuthenticatedClientSessions().size();
                    };
                }
    
                @Override
                public Class<Integer> getResultClass() {
                    return Integer.class;
                }
            };
        }
