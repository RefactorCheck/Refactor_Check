public class nacos_0023 {

    private static final String INVALID_PAGE_ARGUMENT_MESSAGE = "pageNo and pageSize must be greater than zero";

    @Override
    public Page<E> fetchPageLimit(final String sqlFetchRows, Object[] args, final int pageNo,
        final int pageSize,
        final RowMapper rowMapper) {
        if (pageNo <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException(INVALID_PAGE_ARGUMENT_MESSAGE);
        }
        final Page<E> page = new Page<>();
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
