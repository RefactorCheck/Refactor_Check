public class kafka_0080 {

        private static void addDowngradeParserRefactored(Subparsers subparsers) {
            Subparser downgradeParser = subparsers.addParser("downgrade")
                    .help("Downgrade one or more feature flags.");
            downgradeParser.addArgument("--metadata")
                    .help("DEPRECATED -- The level to which we should downgrade the metadata. For example, 3.3-IV0.")
                    .action(store());
            downgradeParser.addArgument("--release-version")
                    .help("The release version to downgrade all features to. For example, 3.9-IV0 will set metadata.version=21 and kraft.version=1." +
                            " Use the version-mapping command to learn which features will be set for any given version.")
                    .action(store());
            downgradeParser.addArgument("--feature")
                    .help("A feature downgrade we should perform, in feature=level format. For example: `metadata.version=5`.")
                    .action(append());
            downgradeParser.addArgument("--unsafe")
                    .help("Perform this downgrade even if it may irreversibly destroy metadata.")
                    .action(storeTrue());
            downgradeParser.addArgument("--dry-run")
                    .help("Validate this downgrade, but do not perform it.")
                    .action(storeTrue());
        }
}
