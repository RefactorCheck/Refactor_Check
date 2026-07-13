public class dubbo_0266 {

        @Override
        public void writeTo(com.google.protobuf.CodedOutputStream output) throws java.io.IOException {
            writeOptionalString(output, 1, group_);
            writeOptionalString(output, 2, version_);
            writeRepeatedString(output, 3, tag_);
            writeRepeatedString(output, 4, service_);
            writeOptionalString(output, 5, openapi_);
            if (((bitField0_ & 0x00000001) != 0)) {
                output.writeEnum(6, format_);
            }
            if (((bitField0_ & 0x00000002) != 0)) {
                output.writeBool(7, pretty_);
            }
            getUnknownFields().writeTo(output);
        }

        private void writeOptionalString(com.google.protobuf.CodedOutputStream output, int fieldNumber, String value) throws java.io.IOException {
            if (!com.google.protobuf.GeneratedMessageV3.isStringEmpty(value)) {
                com.google.protobuf.GeneratedMessageV3.writeString(output, fieldNumber, value);
            }
        }

        private void writeRepeatedString(com.google.protobuf.CodedOutputStream output, int fieldNumber, com.google.protobuf.ProtocolStringList list) throws java.io.IOException {
            for (int i = 0; i < list.size(); i++) {
                com.google.protobuf.GeneratedMessageV3.writeString(output, fieldNumber, list.getRaw(i));
            }
        }
}
