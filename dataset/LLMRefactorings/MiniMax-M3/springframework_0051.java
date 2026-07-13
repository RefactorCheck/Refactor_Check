public class springframework_0051 {

	protected final @Nullable HttpSession getSession(boolean allowCreate) {
		if (isRequestActive()) {
			return retrieveAndStoreSession(allowCreate);
		}
		else {
			// Access through stored session reference, if any...
			HttpSession session = this.session;
			if (session == null) {
				if (allowCreate) {
					throw new IllegalStateException(
							"No session found and request already completed - cannot create new session!");
				}
				else {
					session = retrieveAndStoreSession(false);
				}
			}
			return session;
		}
	}

	private @Nullable HttpSession retrieveAndStoreSession(boolean allowCreate) {
		HttpSession session = this.request.getSession(allowCreate);
		this.session = session;
		return session;
	}
}
