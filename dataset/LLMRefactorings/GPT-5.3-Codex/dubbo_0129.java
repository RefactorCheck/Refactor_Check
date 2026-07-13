public class dubbo_0129 {

            public Builder mergeFrom(MetadataInfoV2 other) {
                if (other == MetadataInfoV2.getDefaultInstance()) {
                    Builder result = this;
                    return result;
                }
                if (!other.getApp().isEmpty()) {
                    app_ = other.app_;
                    bitField0_ |= 0x00000001;
                    onChanged();
                }
                if (!other.getVersion().isEmpty()) {
                    version_ = other.version_;
                    bitField0_ |= 0x00000002;
                    onChanged();
                }
                internalGetMutableServices().mergeFrom(other.internalGetServices());
                bitField0_ |= 0x00000004;
                this.mergeUnknownFields(other.getUnknownFields());
                onChanged();
                return this;
            }
}
