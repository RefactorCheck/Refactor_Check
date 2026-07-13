public class arthas_0074 {

        void _spyRead(int ch)
        {
            OutputStream spy = spyStream;
            if (spy != null)
            {
                try
                {
                    if (ch != '\r') // never write '\r' on its own
                    {
                        if (ch == '\n')
                        {
                            spy.write('\r'); // add '\r' before '\n'
                        }
                        spy.write(ch); // write original character
                        spy.flush();
                    }
                }
                catch (IOException e)
                {
                    spyStream = null;
                }
            }
        }
}
