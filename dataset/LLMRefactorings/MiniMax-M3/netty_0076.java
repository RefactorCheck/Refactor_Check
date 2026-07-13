public class netty_0076 {

    static int setupFlags(boolean useSingleIssuer) {
        int flags = Native.IORING_SETUP_R_DISABLED | Native.IORING_SETUP_CLAMP;
        if (IoUring.isSetupSubmitAllSupported()) {
            flags |= Native.IORING_SETUP_SUBMIT_ALL;
        }

        if (useSingleIssuer) {
            flags = applySingleIssuerFlags(flags);
        }

        // liburing uses IORING_SETUP_NO_SQARRAY by default these days, we should do the same by default if possible.
        // See https://github.com/axboe/liburing/releases/tag/liburing-2.6
        if (IoUring.isIoringSetupNoSqarraySupported()) {
            flags |= Native.IORING_SETUP_NO_SQARRAY;
        }

        // Use IORING_SETUP_CQE_MIXED by default if supported so we can support any OP in the future.
        if (IoUring.isSetupCqeMixedSupported()) {
            flags |= Native.IORING_SETUP_CQE_MIXED;
        }
        return flags;
    }

    private static int applySingleIssuerFlags(int flags) {
        // See https://github.com/axboe/liburing/wiki/io_uring-and-networking-in-2023#task-work
        if (IoUring.isSetupSingleIssuerSupported()) {
            flags |= Native.IORING_SETUP_SINGLE_ISSUER;
        }
        // IORING_SETUP_DEFER_TASKRUN also requires IORING_SETUP_SINGLE_ISSUER.
        if (IoUring.isSetupDeferTaskrunSupported()) {
            flags |= Native.IORING_SETUP_DEFER_TASKRUN;
            flags |= Native.IORING_SETUP_TASKRUN_FLAG;
        }
        return flags;
    }
}
