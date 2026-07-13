public class arthas_0092 {

        void _processSuboption(int suboption[], int suboptionLength)
        throws IOException
        {
            if (debug)
            {
                System.err.println("PROCESS SUBOPTION.");
            }
    
            /* open TelnetOptionHandler functionality (start)*/
            if (suboptionLength > 0)
            {
                if (optionHandlers[suboption[0]] != null)
                {
                    int responseSuboption[] =
                      optionHandlers[suboption[0]].answerSubnegotiation(suboption,
                      suboptionLength);
                    _sendSubnegotiation(responseSuboption);
                }
                else
                {
                    _processUnhandledSuboption(suboption, suboptionLength);
                }
            }
            /* open TelnetOptionHandler functionality (end)*/
        }

        private void _processUnhandledSuboption(int suboption[], int suboptionLength)
        {
            if (suboptionLength > 1)
            {
                if (debug)
                {
                    for (int ii = 0; ii < suboptionLength; ii++)
                    {
                        System.err.println("SUB[" + ii + "]: "
                            + suboption[ii]);
                    }
                }
                if ((suboption[0] == TERMINAL_TYPE)
                    && (suboption[1] == TERMINAL_TYPE_SEND))
                {
                    _sendTerminalType();
                }
            }
        }
}
