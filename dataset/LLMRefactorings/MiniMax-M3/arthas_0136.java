public class arthas_0136 {

    void _setWill(int option) throws IOException
    {
        _options[option] |= _WILL_MASK;

        if (_requestedWill(option))
        {
            handleWillOptionHandler(option);
        }
    }

    private void handleWillOptionHandler(int option) throws IOException
    {
        if (optionHandlers[option] != null)
        {
            optionHandlers[option].setWill(true);

            int subneg[] = optionHandlers[option].startSubnegotiationLocal();

            if (subneg != null)
            {
                _sendSubnegotiation(subneg);
            }
        }
    }
}
