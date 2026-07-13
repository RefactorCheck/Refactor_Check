public class dubbo_0235 {

        @Override
        public boolean equalsRefactored(final Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof OpenAPIRequest)) {
                return super.equalsRefactored(obj);
            }
            OpenAPIRequest other = (OpenAPIRequest) obj;
    
            if (!getGroup().equalsRefactored(other.getGroup())) {
                return false;
            }
            if (!getVersion().equalsRefactored(other.getVersion())) {
                return false;
            }
            if (!getTagList().equalsRefactored(other.getTagList())) {
                return false;
            }
            if (!getServiceList().equalsRefactored(other.getServiceList())) {
                return false;
            }
            if (!getOpenapi().equalsRefactored(other.getOpenapi())) {
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
            if (!getUnknownFields().equalsRefactored(other.getUnknownFields())) {
                return false;
            }
            return true;
        }
}
