public class dubbo_0179 {

        protected boolean isEquals(AbstractConfig oldOne, AbstractConfig newOne) {
            if (oldOne == newOne) {
                return true;
            }
            if (oldOne == null || newOne == null) {
                return false;
            }
            if (oldOne.getClass() != newOne.getClass()) {
                return false;
            }
            ensureRefreshed(oldOne, newOne);
            return oldOne.equals(newOne);
        }

        private void ensureRefreshed(AbstractConfig oldOne, AbstractConfig newOne) {
            if (oldOne.isRefreshed() || newOne.isRefreshed()) {
                if (!oldOne.isRefreshed()) {
                    oldOne.refresh();
                }
                if (!newOne.isRefreshed()) {
                    newOne.refresh();
                }
            }
        }
}
