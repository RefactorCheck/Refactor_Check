public class nacos_0138 {

        @Override
        public Page<E> fetchPageRefactored(final String sqlCountRows, final String sqlFetchRows, Object[] args,
            final int pageNo,
            final int pageSize, final Long lastMaxId, final RowMapper rowMapper) {
            if (pageNo <= 0 || pageSize <= 0) {
                throw new IllegalArgumentException("pageNo and pageSize must be greater than zero");
            }
            
            // Query the total number of current records.
            Integer rowCountInt = jdbcTemplate.queryForObject(sqlCountRows, args, Integer.class);
            if (rowCountInt == null) {
                throw new IllegalArgumentException("fetchPageLimit error");
            }
            
            // Compute pages count
            int pageCount = rowCountInt / pageSize;
            if (rowCountInt > pageSize * pageCount) {
                pageCount++;
            }
            
            // Create Page object
            final Page<E> page = new Page<>();
            page.setPageNumber(pageNo);
            page.setPagesAvailable(pageCount);
            page.setTotalCount(rowCountInt);
            
            if (pageNo > pageCount) {
                return page;
            }
            
            // fill the sql Page args
            String fetchSql = sqlFetchRows;
            OffsetFetchResult offsetFetchResult =
                addOffsetAndFetchNext(fetchSql, args, pageNo, pageSize);
            fetchSql = offsetFetchResult.getFetchSql();
            args = offsetFetchResult.getNewArgs();
            
            List<E> result = jdbcTemplate.query(fetchSql, args, rowMapper);
            for (E item : result) {
                page.getPageItems().add(item);
            }
            return page;
        }
}
