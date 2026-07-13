public class dubbo_0235 {

        @Override
        public boolean equals(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof OpenAPIRequest)) {
                return super.equals(obj);
            }
            OpenAPIRequest other = (OpenAPIRequest) obj;
            return compareFields(other);
        }

        private boolean compareFields(OpenAPIRequest other) {
            if (!getGroup().equals(other.getGroup())) {
                return false;
            }
            if (!getVersion().equals(other.getVersion())) {
                return false;
            }
            if (!getTagList().equals(other.getTagList())) {
                return false;
            }
            if (!getServiceList().equals(other.getServiceList())) {
                return false;
            }
            if (!getOpenapi().equals(other.getOpenapi())) {
                return false;
            }
            if (hasFormat() != other.hasFormat()) {
                return false;
            }
            if (hasFormat()) {
                if (format_ != other.format_) {
                    return false;
                }
            }
            if (hasPretty() != other.hasPretty()) {
                return false;
            }
            if (hasPretty()) {
                if (getPretty() != other.getPretty()) {
                    return false;
                }
            }
            if (!getUnknownFields().equals(other.getUnknownFields())) {
                return false;
            }
            return true;
        }
}
