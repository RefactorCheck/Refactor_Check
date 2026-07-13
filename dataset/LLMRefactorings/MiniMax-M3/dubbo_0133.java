public class dubbo_0133 {

        @Override
        public int getSerializedSize() {
            int size = memoizedSize;
            if (size != -1) {
                return size;
            }
    
            size = 0;
            if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(app_)) {
                size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, app_);
            }
            if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(version_)) {
                size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, version_);
            }
            size += getServicesSerializedSize();
            size += getUnknownFields().getSerializedSize();
            memoizedSize = size;
            return size;
        }

        private int getServicesSerializedSize() {
            int size = 0;
            for (java.util.Map.Entry<String, ServiceInfoV2> entry :
                    internalGetServices().getMap().entrySet()) {
                com.google.protobuf.MapEntry<String, ServiceInfoV2> services__ = ServicesDefaultEntryHolder.defaultEntry
                        .newBuilderForType()
                        .setKey(entry.getKey())
                        .setValue(entry.getValue())
                        .build();
                size += com.google.protobuf.CodedOutputStream.computeMessageSize(3, services__);
            }
            return size;
        }
}
