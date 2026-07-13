public class netty_0129 {

        @Override
        public boolean equals(Object oValue) {
            if (this == oValue) {
                return true;
            }
            if (oValue == null || getClass() != oValue.getClass()) {
                return false;
            }
    
            MqttSubscriptionOption that = (MqttSubscriptionOption) oValue;
    
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
