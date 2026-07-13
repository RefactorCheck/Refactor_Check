public class keycloak_0179 {

    private static final String USAGE = "Usage: " + CMD + " set-password (--username USERNAME | --userid ID) [--new-password PASSWORD] [ARGUMENTS]";
    private static final String COMMAND_DESCRIPTION = "Command to reset user's password.";
    private static final String CREDENTIALS_HINT = "Use `" + CMD + " config credentials` to establish an authenticated session, or use CREDENTIALS OPTIONS to perform one time authentication.";
    private static final String ARGUMENT_OPTIONS =
            "    --username USERNAME       Identify target user by 'username'\n" +
            "    --userid ID               Identify target user by 'id'\n" +
            "    -p, --new-password        New password to set. If not specified and the env variable KC_CLI_PASSWORD is not defined, you will be prompted for it.\n" +
            "    -t, --temporary           Make the new password temporary - user has to change it on next logon\n" +
            "    -a, --admin-root URL      URL of Admin REST endpoint root if not default - e.g. http://localhost:8080/admin\n" +
            "    -r, --target-realm REALM  Target realm to issue requests against if not the one authenticated against";
    private static final String EXAMPLES_HEADER = "Examples:";
    private static final String EXAMPLE_TEMPORARY_PASSWORD = "  " + PROMPT + " " + CMD + " set-password -r demorealm --username testuser --new-password NEWPASS -t";
    private static final String GENERAL_HELP_HINT = "Use '" + CMD + " help' for general information and a list of commands";

    @Override
    protected String help() {
        StringWriter sb = new StringWriter();
        PrintWriter out = new PrintWriter(sb);
        out.println(USAGE);
        out.println();
        out.println(COMMAND_DESCRIPTION);
        out.println();
        out.println(CREDENTIALS_HINT);
        globalOptions(out);
        out.println(ARGUMENT_OPTIONS);
        out.println();
        out.println(EXAMPLES_HEADER);
        out.println();
        out.println("Set new temporary password for the user:");
        out.println(EXAMPLE_TEMPORARY_PASSWORD);
        out.println();
        out.println();
        out.println(GENERAL_HELP_HINT);
        return sb.toString();
    }
}
