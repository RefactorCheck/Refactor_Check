public class springframework_0164 {

    	@Override
    	public void initializeWithMetaData(DatabaseMetaData databaseMetaData) throws SQLException {
    		setMetadataFlag(() -> databaseMetaData.supportsCatalogsInProcedureCalls(), this::setSupportsCatalogsInProcedureCalls, "supportsCatalogsInProcedureCalls");
    		setMetadataFlag(() -> databaseMetaData.supportsSchemasInProcedureCalls(), this::setSupportsSchemasInProcedureCalls, "supportsSchemasInProcedureCalls");
    		setMetadataFlag(() -> databaseMetaData.storesUpperCaseIdentifiers(), this::setStoresUpperCaseIdentifiers, "storesUpperCaseIdentifiers");
    		setMetadataFlag(() -> databaseMetaData.storesLowerCaseIdentifiers(), this::setStoresLowerCaseIdentifiers, "storesLowerCaseIdentifiers");
    	}

    	private void setMetadataFlag(SqlBooleanSupplier getter, Consumer<Boolean> setter, String propertyName) {
    		try {
    			setter.accept(getter.getAsBoolean());
    		}
    		catch (SQLException ex) {
    			logger.debug("Error retrieving 'DatabaseMetaData." + propertyName + "' - " + ex.getMessage());
    		}
    	}

    	@FunctionalInterface
    	interface SqlBooleanSupplier {
    		boolean getAsBoolean() throws SQLException;
    	}
}
