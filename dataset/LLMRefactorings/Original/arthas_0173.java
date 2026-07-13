public class arthas_0173 {

        void _processDont(int option) throws IOException
        {
            if (debugoptions)
            {
                System.err.println("RECEIVED DONT: "
                    + TelnetOption.getOption(option));
            }
            if (__notifhand != null)
            {
                __notifhand.receivedNegotiation(
                    TelnetNotificationHandler.RECEIVED_DONT,
                    option);
            }
            if (_willResponse[option] > 0)
            {
                --_willResponse[option];
                if (_willResponse[option] > 0 && _stateIsWont(option))
                {
                    --_willResponse[option];
                }
            }
    
            if (_willResponse[option] == 0 && _requestedWill(option))
            {
    
                switch (option)
                {
    
                default:
                    break;
    
                }
    
                /* FIX for a BUG in the negotiation (start)*/
                if ((_stateIsWill(option)) || (_requestedWill(option)))
                {
                    _sendWont(option);
                }
    
                _setWantWont(option);
                /* FIX for a BUG in the negotiation (end)*/
            }
    
            _setWont(option);
        }
}
