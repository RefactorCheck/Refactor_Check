public LdapName renameEntry(LdapName oldDn, LdapName newDn, boolean fallback) {
            try {
                LdapName newNonConflictingDn = execute(new LdapOperation<LdapName>() {
    
                    @Override
                    public LdapName execute(LdapContext context) throws NamingException {
                        LdapName dn = newDn;
    
                        // Max 5 attempts for now
                        int max = 5;
                        for (int i=0 ; i<max ; i++) {
                            try {
                                context.rename(oldDn, dn);
                                return extractRenameEntry(oldDn, newDn, fallback);
                            } catch (NameAlreadyBoundException ex) {
                                if (!fallback) {
                                    throw ex;
                                } else {
                                    LdapName failedDn = dn;
                                    if (i<max) {
                                        dn = findNextDNForFallback(newDn, i);
                                        logger.warnf("Failed to rename DN [%s] to [%s]. Will try to fallback to DN [%s]", oldDn, failedDn, dn);
                                    } else {
                                        logger.warnf("Failed all fallbacks for renaming [%s]", oldDn);
                                        throw ex;
                                    }
                                }
                            }
                        }
    
                        throw new ModelException("Could not rename entry from DN [" + oldDn + "] to new DN [" + newDn + "]. All fallbacks failed");
                    }
    
    
                    @Override
                    public String toString() {
                        return new StringBuilder("LdapOperation: renameEntry\n")
                                .append(" oldDn: ").append(oldDn).append("\n")
                                .append(" newDn: ").append(newDn)
                                .toString();
                    }
    
                });
                return newNonConflictingDn;
            } catch (NamingException e) {
                throw new ModelException("Could not rename entry from DN [" + oldDn + "] to new DN [" + newDn + "]", e);
            }
        }
