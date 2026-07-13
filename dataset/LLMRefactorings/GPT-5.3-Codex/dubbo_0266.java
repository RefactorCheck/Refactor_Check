public class dubbo_0266 {

        @Override
        public void writeTo(final com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
            if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(group_)) {
                com.google.protobuf.GeneratedMessageV3.writeString(output, 1, group_);
            }
            if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(version_)) {
                com.google.protobuf.GeneratedMessageV3.writeString(output, 2, version_);
            }
            for (int i = 0; i < tag_.size(); i++) {
                com.google.protobuf.GeneratedMessageV3.writeString(output, 3, tag_.getRaw(i));
            }
            for (int i = 0; i < service_.size(); i++) {
                com.google.protobuf.GeneratedMessageV3.writeString(output, 4, service_.getRaw(i));
            }
            if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(openapi_)) {
                com.google.protobuf.GeneratedMessageV3.writeString(output, 5, openapi_);
            }
            if (((bitField0_ & 0x00000001) != 0)) {
                output.writeEnum(6, format_);
            }
            if (((bitField0_ & 0x00000002) != 0)) {
                output.writeBool(7, pretty_);
            }
            getUnknownFields().writeTo(output);
        }
}
