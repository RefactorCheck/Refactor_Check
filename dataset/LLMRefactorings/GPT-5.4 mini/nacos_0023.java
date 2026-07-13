public class nacos_0023 {

        @Override
        public Page<E> fetchPageLimitRefactored(final String sqlFetchRows, Object[] args, final int pageNo,
            final int pageSize,
            final RowMapper rowMapper) {
            if (pageNo <= 0 || pageSize <= 0) {
                throw new IllegalArgumentException("pageNo and pageSize must be greater than zero");
            }
            // Create Page object
            final Page<E> page = new Page<>();
            
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
