public class springframework_0092 {

	private Object loadWebLogicTransactionHelper() throws TransactionSystemException {
		Object helper = this.transactionHelper;
		if (helper == null) {
			helper = doLoadWebLogicTransactionHelper();
			this.transactionHelper = helper;
		}
		return helper;
	}

	private Object doLoadWebLogicTransactionHelper() throws TransactionSystemException {
		try {
			Class<?> transactionHelperClass = getClass().getClassLoader().loadClass(TRANSACTION_HELPER_CLASS_NAME);
			Method getTransactionHelperMethod = transactionHelperClass.getMethod("getTransactionHelper");
			Object helper = getTransactionHelperMethod.invoke(null);
			logger.trace("WebLogic TransactionHelper found");
			return helper;
		}
		catch (InvocationTargetException ex) {
			throw new TransactionSystemException(
					"WebLogic's TransactionHelper.getTransactionHelper() method failed", ex.getTargetException());
		}
		catch (Exception ex) {
			throw new TransactionSystemException(
					"Could not initialize WebLogicJtaTransactionManager because WebLogic API classes are not available",
					ex);
		}
	}
}
