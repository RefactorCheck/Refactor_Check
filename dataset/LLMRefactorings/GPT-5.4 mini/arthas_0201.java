public class arthas_0201 {

        void _processWillRefactored(int option) throws IOException
        {
            if (debugoptions)
            {
                System.err.println("RECEIVED WILL: "
                    + TelnetOption.getOption(option));
            }
    
            if (__notifhand != null)
            {
                __notifhand.receivedNegotiation(
                    TelnetNotificationHandler.RECEIVED_WILL,
                    option);
            }
    
            boolean acceptNewState = false;
    
            /* open TelnetOptionHandler functionality (start)*/
            if (optionHandlers[option] != null)
            {
                acceptNewState = optionHandlers[option].getAcceptRemote();
            }
            /* open TelnetOptionHandler functionality (end)*/
    
            if (_doResponse[option] > 0)
            {
                --_doResponse[option];
                if (_doResponse[option] > 0 && _stateIsDo(option))
                {
                    --_doResponse[option];
                }
            }
    
            if (_doResponse[option] == 0 && _requestedDont(option))
            {
    
                switch (option)
                {
    
                default:
                    break;
    
                }
    
    
                if (acceptNewState)
                {
                    _setWantDo(option);
                    _sendDo(option);
                }
                else
                {
                    ++_doResponse[option];
                    _sendDont(option);
                }
            }
    
            _setDo(option);
        }
}
