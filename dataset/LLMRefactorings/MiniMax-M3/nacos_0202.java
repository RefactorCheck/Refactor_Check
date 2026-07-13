public class nacos_0202 {

    private Page<E> doFetchPage(final String sqlCountRows, final Object[] countAgrs,
        final String sqlFetchRows,
        final Object[] fetchArgs, final int pageNo, final int pageSize, final RowMapper rowMapper) {
        checkPageInfo(pageNo, pageSize);
        Integer rowCountInt = null;
        if (null != countAgrs) {
            rowCountInt = databaseOperate.queryOne(sqlCountRows, countAgrs, Integer.class);
        } else {
            rowCountInt = databaseOperate.queryOne(sqlCountRows, Integer.class);
        }
        if (rowCountInt == null) {
            throw new IllegalArgumentException("fetchPageLimit error");
        }
        int pageCount = calculatePageCount(rowCountInt, pageSize);
        final Page<E> page = new Page<>();
        page.setPageNumber(pageNo);
        page.setPagesAvailable(pageCount);
        page.setTotalCount(rowCountInt);
        if (pageNo > pageCount) {
            return page;
        }
        List<E> result = databaseOperate.queryMany(sqlFetchRows, fetchArgs, rowMapper);
        for (E item : result) {
            page.getPageItems().add(item);
        }
        return page;
    }

    private int calculatePageCount(int rowCountInt, int pageSize) {
        int pageCount = rowCountInt / pageSize;
        if (rowCountInt > pageSize * pageCount) {
            pageCount++;
        }
        return pageCount;
    }
}
