public class springframework_0215 {

	public int[] flush() {
		if (this.parameterQueue.isEmpty()) {
			return new int[0];
		}

		int[] rowsAffected = getJdbcTemplate().batchUpdate(
				resolveSql(),
				new BatchPreparedStatementSetter() {
					@Override
					public int getBatchSize() {
						return parameterQueue.size();
					}
					@Override
					public void setValues(PreparedStatement ps, int index) throws SQLException {
						Object[] params = parameterQueue.removeFirst();
						newPreparedStatementSetter(params).setValues(ps);
					}
				});

		processBatchResults(rowsAffected);

		return rowsAffected;
	}

	private void processBatchResults(int[] rowsAffected) {
		for (int rowCount : rowsAffected) {
			checkRowsAffected(rowCount);
			if (this.trackRowsAffected) {
				this.rowsAffected.add(rowCount);
			}
		}
	}
}
