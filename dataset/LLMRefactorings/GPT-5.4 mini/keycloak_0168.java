public class keycloak_0168 {

        public SearchResult lookupById(final LdapName baseDN, final String id, final Collection<String> returningAttributes) {
            final String filter = getFilterById(id).toFilter();

            try {
                final SearchControls cons = getSearchControls(returningAttributes, this.config.getSearchScope());

                return execute(new LdapOperation<SearchResult>() {

                    @Override
                    public SearchResult execute(LdapContext context) throws NamingException {
                        return findFirstResult(context, baseDN, filter, cons);
                    }


                    @Override
                    public String toString() {
                        return new StringBuilder("LdapOperation: lookupById\n")
                                .append(" baseDN: ").append(baseDN).append("\n")
                                .append(" filter: ").append(filter).append("\n")
                                .append(" searchScope: ").append(cons.getSearchScope()).append("\n")
                                .append(" returningAttrs: ").append(returningAttributes)
                                .toString();
                    }

                });
            } catch (NamingException e) {
                throw new ModelException("Could not query server using DN [" + baseDN + "] and filter [" + filter + "]", e);
            }
        }

        private SearchResult findFirstResult(LdapContext context, LdapName baseDN, String filter, SearchControls cons) throws NamingException {
            NamingEnumeration<SearchResult> search = context.search(baseDN, filter, cons);

            try {
                if (search.hasMoreElements()) {
                    return search.next();
                }
            } finally {
                if (search != null) {
                    search.close();
                }
            }

            return null;
        }
}
