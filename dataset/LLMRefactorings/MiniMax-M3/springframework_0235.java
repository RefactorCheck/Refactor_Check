public class springframework_0235 {

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		EntityManagerFactory emf = lookupEntityManagerFactory(request);
		boolean participate = false;

		WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
		String key = getAlreadyFilteredAttributeName();

		if (TransactionSynchronizationManager.hasResource(emf)) {
			participate = true;
		}
		else {
			boolean isFirstRequest = !isAsyncDispatch(request);
			if (isFirstRequest || !applyEntityManagerBindingInterceptor(asyncManager, key)) {
				bindEntityManager(emf, asyncManager, key);
			}
		}

		try {
			filterChain.doFilter(request, response);
		}

		finally {
			if (!participate) {
				EntityManagerHolder emHolder = (EntityManagerHolder)
						TransactionSynchronizationManager.unbindResource(emf);
				if (!isAsyncStarted(request)) {
					logger.debug("Closing JPA EntityManager in OpenEntityManagerInViewFilter");
					EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());
				}
			}
		}
	}

	private void bindEntityManager(EntityManagerFactory emf, WebAsyncManager asyncManager, String key) {
		logger.debug("Opening JPA EntityManager in OpenEntityManagerInViewFilter");
		try {
			EntityManager em = createEntityManager(emf);
			EntityManagerHolder emHolder = new EntityManagerHolder(em);
			TransactionSynchronizationManager.bindResource(emf, emHolder);

			AsyncRequestInterceptor interceptor = new AsyncRequestInterceptor(emf, emHolder);
			asyncManager.registerCallableInterceptor(key, interceptor);
			asyncManager.registerDeferredResultInterceptor(key, interceptor);
		}
		catch (PersistenceException ex) {
			throw new DataAccessResourceFailureException("Could not create JPA EntityManager", ex);
		}
	}
}
