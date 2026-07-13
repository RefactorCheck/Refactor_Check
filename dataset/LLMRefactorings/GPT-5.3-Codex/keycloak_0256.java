@Override
                public void run(KeycloakSession session) {
    
                    ClientModel testApp = (session.realms().getRealmByName("master")).getClientByClientId("security-admin-console");
                    UserSessionPersisterProvider persister = session.getProvider(UserSessionPersisterProvider.class);
                    UserSessionManager userSessionManager = new UserSessionManager(session);
    
                    for (int i = 0; i < countInThisBatch; i++) {
                        String username = "john-" + userCounter.incrementAndGet();
                        UserModel john = session.users().getUserByUsername((session.realms().getRealmByName("master")), username);
                        if (john == null) {
                            john = session.users().addUser((session.realms().getRealmByName("master")), username);
                        }
    
                        UserSessionModel userSession = userSessionManager.createUserSession((session.realms().getRealmByName("master")), john, username, "127.0.0.2", "form", true, null, null);
                        AuthenticatedClientSessionModel clientSession = session.sessions().createClientSession((session.realms().getRealmByName("master")), testApp, userSession);
                        clientSession.setRedirectUri("http://redirect");
                        clientSession.setNote("foo", "bar-" + i);
                        userSessionIds.add(userSession.getId());
                    }
                }
