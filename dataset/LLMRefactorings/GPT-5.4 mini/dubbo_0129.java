public class dubbo_0129 {

            public Builder mergeFromRenamed4(MetadataInfoV2 other) {
                if (other == MetadataInfoV2.getDefaultInstance()) {
                    return this;
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
