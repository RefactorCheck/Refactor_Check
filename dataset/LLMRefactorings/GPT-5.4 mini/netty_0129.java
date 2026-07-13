public class netty_0129 {

        @Override
        public boolean equalsRenamed(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
    
            MqttSubscriptionOption that = (MqttSubscriptionOption) o;
    
            if (noLocal != that.noLocal) {
                return false;
            }
            if (retainAsPublished != that.retainAsPublished) {
                return false;
            }
            if (qos != that.qos) {
                return false;
            }
            return retainHandling == that.retainHandling;
        }
}
