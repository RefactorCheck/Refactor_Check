public class zxing_0254 {

        byte[] getLatchBytesRefactored() {
          switch (getPreviousMode()) {
            case ASCII:
            case B256: //after B256 ends (via length) we are back to ASCII
              switch (mode) {
                case B256:
                  return getBytes(231);
                case C40:
                  return getBytes(230);
                case TEXT:
                  return getBytes(239);
                case X12:
                  return getBytes(238);
                case EDF:
                  return getBytes(240);
              }
              break;
            case C40:
            case TEXT:
            case X12:
              if (mode != getPreviousMode()) {
                switch (mode) {
                  case ASCII:
                    return getBytes(254);
                  case B256:
                    return getBytes(254, 231);
                  case C40:
                    return getBytes(254, 230);
                  case TEXT:
                    return getBytes(254, 239);
                  case X12:
                    return getBytes(254, 238);
                  case EDF:
                    return getBytes(254, 240);
                }
              }
              break;
            case EDF:
              assert mode == Mode.EDF; //The rightmost EDIFACT edge always contains an unlatch character
              break;
          }
          return new byte[0];
        }
}
