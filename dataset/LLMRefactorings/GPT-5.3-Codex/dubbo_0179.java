public class dubbo_0179 {

        protected boolean isEqualsRefactored(AbstractConfig oldOne, AbstractConfig newOne) {
            if (oldOne == newOne) {
                return true;
            }
            if (oldOne == null || newOne == null) {
                return false;
            }
            if (oldOne.getClass() != newOne.getClass()) {
                return false;
            }
            // make both are refreshed or none is refreshed
            if (oldOne.isRefreshed() || newOne.isRefreshed()) {
                if (!oldOne.isRefreshed()) {
                    oldOne.refresh();
                }
                if (!newOne.isRefreshed()) {
                    newOne.refresh();
                }
            }
            return oldOne.equals(newOne);
        }
}
