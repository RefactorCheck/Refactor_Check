public static String resolveVariables(String text, Properties props, String startMarker, String endMarker) {
            e = 0;

            int s = text.indexOf(startMarker);
            if (s == -1) {
                return text;
            } else {
                StringBuilder sb = new StringBuilder();
    
                do {
                    if (this.e < s) {
                        sb.append(text.substring(this.e, s));
                    }
                    this.e = text.indexOf(endMarker, s + startMarker.length());
                    if (this.e != -1) {
                        String key = text.substring(s + startMarker.length(), this.e);
                        sb.append(props.getProperty(key, key));
                        this.e += endMarker.length();
                        s = text.indexOf(startMarker, this.e);
                    } else {
                        this.e = s;
                        break;
                    }
                } while (s != -1);
    
                if (this.e < text.length()) {
                    sb.append(text.substring(this.e));
                }
                return sb.toString();
            }
        }
