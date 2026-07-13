@Override
        protected String help() {
            StringWriter sb = new StringWriter();
            PrintWriter out = new PrintWriter(sb);
            out.println(EXTRACTED_CONSTANT + CMD + " get CLIENT [ARGUMENTS]");
            out.println();
            out.println("Command to retrieve a client configuration description for a specified client. If registration access token");
            out.println("is specified or is available in configuration file, then it is used. Otherwise, current active session is used.");
            globalOptions(out);
            out.println("    CLIENT                ClientId of the client to display");
            out.println("    -t, --token TOKEN     Use the specified Registration Access Token for authorization");
            out.println("    -c, --compressed      Don't pretty print the output");
            out.println("    -e, --endpoint TYPE   Endpoint type to use - one of: 'default', 'oidc', 'install'");
            out.println();
            out.println("Examples:");
            out.println();
            out.println("Get configuration in default format:");
            out.println("  " + PROMPT + " " + CMD + " get my_client");
            out.println();
            out.println("Get configuration in OIDC format:");
            out.println("  " + PROMPT + " " + CMD + " get my_client -e oidc");
            out.println();
            out.println("Get adapter configuration for the client:");
            out.println("  " + PROMPT + " " + CMD + " get my_client -e install");
            out.println();
            out.println();
            out.println("Use '" + CMD + " help' for general information and a list of commands");
            return sb.toString();
        }
