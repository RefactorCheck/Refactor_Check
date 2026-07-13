public class netty_0151 {

        private static void writeParameters(List<CharSequence> parameters, ByteBuf out, boolean commandNotEmpty) {
            if (parameters.isEmpty()) {
                return;
            }
            if (commandNotEmpty) {
                out.writeByte(SP);
            }
            if (parameters instanceof RandomAccess) {
                final int sizeMinusOne = parameters.size() - 1;
                for (int i = 0; i < sizeMinusOne; i++) {
                    writeParameterWithSeparator(out, parameters.get(i));
                }
                ByteBufUtil.writeAscii(out, parameters.get(sizeMinusOne));
            } else {
                final Iterator<CharSequence> params = parameters.iterator();
                while (params.hasNext()) {
                    CharSequence param = params.next();
                    ByteBufUtil.writeAscii(out, param);
                    if (params.hasNext()) {
                        out.writeByte(SP);
                    }
                }
            }
        }

        private static void writeParameterWithSeparator(ByteBuf out, CharSequence param) {
            ByteBufUtil.writeAscii(out, param);
            out.writeByte(SP);
        }
}
