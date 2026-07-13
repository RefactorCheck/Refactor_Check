public class netty_0129 {

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
    
            return hasSameStateAs((MqttSubscriptionOption) o);
        }

        private boolean hasSameStateAs(MqttSubscriptionOption that) {
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
