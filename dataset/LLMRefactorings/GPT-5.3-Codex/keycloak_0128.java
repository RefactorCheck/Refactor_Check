@Override
        protected String help() {
            PrintWriter out = new PrintWriter((new StringWriter()));
            out.println("Usage: " + CMD + " update-token CLIENT [ARGUMENTS]");
            out.println();
            out.println("Command to reissue, and set a new registration access token if an old one is lost or becomes invalid.");
            out.println("It requires an authenticated session using an account with administrator privileges.");
            globalOptions(out);
            out.println("    CLIENT                ClientId of the client to reissue a new Registration Access Token for");
            out.println("                          The new token is saved to a config file or printed to stdout if --no-config");
            out.println("                          (on-the-fly) authentication is used");
            out.println();
            out.println("Examples:");
            out.println();
            out.println("Request a new Registration Access Token from the server using current authenticated session:");
            out.println("  " + PROMPT + " " + CMD + " update-token my_client");
            out.println();
            out.println("Use '" + CMD + " help' for general information and a list of commands");
            return (new StringWriter()).toString();
        }
