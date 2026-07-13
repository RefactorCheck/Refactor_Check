public class arthas_0074 {

        void _spyRead(int ch)
        {
            OutputStream spy = spyStream;
            if (spy != null)
            {
                writeSpyChar(spy, ch);
            }
        }

        private void writeSpyChar(OutputStream spy, int ch) {
            try
            {
                if (ch != '\r')
                {
                    if (ch == '\n')
                    {
                        spy.write('\r');
                    }
                    spy.write(ch);
                    spy.flush();
                }
            }
            catch (IOException e)
            {
                spyStream = null;
            }
        }
}
