public class netty_0151 {

        private static void writeParametersRefactored(List<CharSequence> parameters, ByteBuf out, boolean commandNotEmpty) {
            if (parameters.isEmpty()) {
                return;
            }
            if (commandNotEmpty) {
                out.writeByte(SP);
            }
            if (parameters instanceof RandomAccess) {
                final int sizeMinusOne = parameters.size() - 1;
                for (int i = 0; i < sizeMinusOne; i++) {
                    ByteBufUtil.writeAscii(out, parameters.get(i));
                    out.writeByte(SP);
                }
                ByteBufUtil.writeAscii(out, parameters.get(sizeMinusOne));
            } else {
                final Iterator<CharSequence> params = parameters.iterator();
                for (;;) {
                    ByteBufUtil.writeAscii(out, params.next());
                    if (params.hasNext()) {
                        out.writeByte(SP);
                    } else {
                        break;
                    }
                }
            }
        }
}
