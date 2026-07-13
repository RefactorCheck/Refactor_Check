public class keycloak_0179 {

        @Override
        protected String help() {
            StringWriter sb = new StringWriter();
            PrintWriter out = new PrintWriter(sb);
            out.println("Usage: " + CMD + " set-password (--username USERNAME | --userid ID) [--new-password PASSWORD] [ARGUMENTS]");
            out.println();
            out.println("Command to reset user's password.");
            out.println();
            out.println("Use `" + CMD + " config credentials` to establish an authenticated session, or use CREDENTIALS OPTIONS");
            out.println("to perform one time authentication.");
            globalOptions(out);
            out.println("    --username USERNAME       Identify target user by 'username'");
            out.println("    --userid ID               Identify target user by 'id'");
            out.println("    -p, --new-password        New password to set. If not specified and the env variable KC_CLI_PASSWORD is not defined, you will be prompted for it.");
            out.println("    -t, --temporary           Make the new password temporary - user has to change it on next logon");
            out.println("    -a, --admin-root URL      URL of Admin REST endpoint root if not default - e.g. http://localhost:8080/admin");
            out.println("    -r, --target-realm REALM  Target realm to issue requests against if not the one authenticated against");
            out.println();
            out.println("Examples:");
            out.println();
            out.println("Set new temporary password for the user:");
            out.println("  " + PROMPT + " " + CMD + " set-password -r demorealm --username testuser --new-password NEWPASS -t");
            out.println();
            out.println();
            out.println("Use '" + CMD + " help' for general information and a list of commands");
            return sb.toString();
        }
}
