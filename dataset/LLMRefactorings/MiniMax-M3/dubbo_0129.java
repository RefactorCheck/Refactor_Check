public class dubbo_0129 {

    private static final int APP_BIT = 0x00000001;
    private static final int VERSION_BIT = 0x00000002;
    private static final int SERVICES_BIT = 0x00000004;

    public Builder mergeFrom(MetadataInfoV2 other) {
        if (other == MetadataInfoV2.getDefaultInstance()) {
            return this;
        }
        if (!other.getApp().isEmpty()) {
            app_ = other.app_;
            bitField0_ |= APP_BIT;
            onChanged();
        }
        if (!other.getVersion().isEmpty()) {
            version_ = other.version_;
            bitField0_ |= VERSION_BIT;
            onChanged();
        }
        internalGetMutableServices().mergeFrom(other.internalGetServices());
        bitField0_ |= SERVICES_BIT;
        this.mergeUnknownFields(other.getUnknownFields());
        onChanged();
        return this;
    }
}
