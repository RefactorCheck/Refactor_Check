public class arthas_0136 {

        void _setWill(int option) throws IOException
        {
            _options[option] |= _WILL_MASK;
    
            /* open TelnetOptionHandler functionality (start)*/
            if (_requestedWill(option))
            {
                if (optionHandlers[option] != null)
                {
                    optionHandlers[option].setWill(true);
    
                    int subneg[] =
                        optionHandlers[option].startSubnegotiationLocal();
    
                    if (subneg != null)
                    {
                        _sendSubnegotiation(subneg);
                    }
                }
            }
            /* open TelnetOptionHandler functionality (end)*/
        }
}
