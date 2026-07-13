public class zxing_0188 {
    int encodeChar(char c, StringBuilder sb) {
        if (c == ' ') {
            sb.append('\3');
            return 1;
        }
        if (c >= '0' && c <= '9') {
            sb.append((char) (c - 48 + 4));
            return 1;
        }
        if (c >= 'A' && c <= 'Z') {
            sb.append((char) (c - 65 + 14));
            return 1;
        }
        if (c < ' ') {
            sb.append('\0');
            sb.append(c);
            return 2;
        }
        if (c <= '/') {
            return appendShift2Set(sb, (char) (c - 33));
        }
        if (c <= '@') {
            return appendShift2Set(sb, (char) (c - 58 + 15));
        }
        if (c <= '_') {
            return appendShift2Set(sb, (char) (c - 91 + 22));
        }
        if (c <= 127) {
            sb.append('\2');
            sb.append((char) (c - 96));
            return 2;
        }
        sb.append("\1\u001e");
        int len = 2;
        len += encodeChar((char) (c - 128), sb);
        return len;
    }
    
    private int appendShift2Set(StringBuilder sb, char c) {
        sb.append('\1');
        sb.append(c);
        return 2;
    }
}
