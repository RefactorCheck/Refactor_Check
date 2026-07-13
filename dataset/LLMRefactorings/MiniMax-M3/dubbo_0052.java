public class URL {

        @Override
        public int hashCode() {
            if (memoizedHashCode != 0) {
                return memoizedHashCode;
            }
            int hash = 41;
            hash = (19 * hash) + getDescriptor().hashCode();
            hash = (37 * hash) + NAME_FIELD_NUMBER;
            hash = (53 * hash) + getName().hashCode();
            hash = (37 * hash) + GROUP_FIELD_NUMBER;
            hash = (53 * hash) + getGroup().hashCode();
            hash = (37 * hash) + VERSION_FIELD_NUMBER;
            hash = (53 * hash) + getVersion().hashCode();
            hash = (37 * hash) + PROTOCOL_FIELD_NUMBER;
            hash = (53 * hash) + getProtocol().hashCode();
            hash = (37 * hash) + PORT_FIELD_NUMBER;
            hash = (53 * hash) + getPort();
            hash = (37 * hash) + PATH_FIELD_NUMBER;
            hash = (53 * hash) + getPath().hashCode();
            if (!internalGetParams().getMap().isEmpty()) {
                hash = (37 * hash) + PARAMS_FIELD_NUMBER;
                hash = (53 * hash) + internalGetParams().hashCode();
            }
            hash = (29 * hash) + getUnknownFields().hashCode();
            memoizedHashCode = hash;
            return hash;
        }
}
