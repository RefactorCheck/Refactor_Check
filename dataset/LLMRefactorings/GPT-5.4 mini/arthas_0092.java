public class arthas_0092 {

static         void _processSuboption(int suboption[], int suboptionLength)
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
            /* open TelnetOptionHandler functionality (end)*/
        }
}
