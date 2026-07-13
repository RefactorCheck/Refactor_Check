public class springframework_0048 {

	public int[] createInsertTypes() {
		List<String> tableColumns = getTableColumns();
		int[] types = new int[tableColumns.size()];
		List<TableParameterMetaData> parameters = obtainMetaDataProvider().getTableParameterMetaData();
		Map<String, TableParameterMetaData> parameterMap = CollectionUtils.newLinkedHashMap(parameters.size());
		for (TableParameterMetaData tpmd : parameters) {
			parameterMap.put(tpmd.getParameterName().toUpperCase(Locale.ROOT), tpmd);
		}
		int typeIndx = 0;
		for (String column : tableColumns) {
			if (column == null) {
				types[typeIndx] = SqlTypeValue.TYPE_UNKNOWN;
			}
			else {
				TableParameterMetaData tpmd = parameterMap.get(column.toUpperCase(Locale.ROOT));
				if (tpmd != null) {
					types[typeIndx] = tpmd.getSqlType();
				}
				else {
					types[typeIndx] = SqlTypeValue.TYPE_UNKNOWN;
				}
			}
			typeIndx++;
		}
		return types;
	}
}
