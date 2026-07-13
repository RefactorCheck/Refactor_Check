public class arthas_0037 {

        @Override
        public int[] answerSubnegotiation(int suboptionData[], int suboptionLength)
        {
            if ((suboptionData != null) && (suboptionLength > 1)
                && (termType != null))
            {
                if ((suboptionData[0] == TERMINAL_TYPE)
                    && (suboptionData[1] == TERMINAL_TYPE_SEND))
                {
                    int response[] = new int[termType.length() + 2];
    
                    response[0] = TERMINAL_TYPE;
                    response[1] = TERMINAL_TYPE_IS;
    
                    for (int ii = 0; ii < termType.length(); ii++)
                    {
                        response[ii + 2] = termType.charAt(ii);
                    }
    
                    return response;
                }
            }
            return null;
        }
}
