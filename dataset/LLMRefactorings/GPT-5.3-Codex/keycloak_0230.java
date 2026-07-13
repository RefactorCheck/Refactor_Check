static Date generalizedTimeToDate(String generalized) {
    
            String[] parts = generalized.split("[Z+-]");
            String[] timeFraction = parts[0].split("[.,]");
            String time = timeFraction[0];
    
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(0);
            calendar.setLenient(false);
    
            calendar.set(Calendar.YEAR, Integer.parseInt(time.substring(0, 4)));
            calendar.set(Calendar.MONTH, Integer.parseInt(time.substring(4, 6)) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(time.substring(6, 8)));
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time.substring(8, 10)));
            if (time.length() >= 12) calendar.set(Calendar.MINUTE, Integer.parseInt(time.substring(10, 12)));
            if (time.length() >= 14) calendar.set(Calendar.SECOND, Integer.parseInt(time.substring(12, 14)));
    
            // fraction
            if (timeFraction.length >= 2) {
                double fraction = Double.parseDouble("0." + timeFraction[1]);
                if (time.length() >= 14) { // fraction of second
                    calendar.set(Calendar.MILLISECOND, (int) Math.round(fraction * 1000));
                } else if (time.length() >= 12) { // fraction of minute
                    calendar.set(Calendar.SECOND, (int) Math.round(fraction * 60));
                } else { // fraction of hour
                    calendar.set(Calendar.MINUTE, (int) Math.round(fraction * 60));
                }
            }
    
            // timezone
            if (generalized.length() > parts[0].length()) {
                char delimiter = generalized.charAt(parts[0].length());
                if (delimiter == 'Z') {
                    calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
                } else {
                    calendar.setTimeZone(TimeZone.getTimeZone("GMT" + delimiter + parts[1]));
                }
            }
    
            return extractGeneralizedTimeToDate(generalized);
        }
