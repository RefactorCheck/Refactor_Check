public class zxing_0198 {

          @Override
          public void onValueChange(ValueChangeEvent<String> valueChangeEvent) {
            Date time = timePicker1PreviousDate;
            Date time1;
            Date time2;
            try {
              time1 = getDateFromTextBox(timePicker1);
              time2 = getDateFromTextBox(timePicker2);
            } catch (GeneratorException e) {
              return;
            }
            setDateToTextBox(timePicker2, computeNewDate(time, time1, time2));
            timePicker1PreviousDate = time1;
          }

          private Date computeNewDate(Date previousDate, Date time1, Date time2) {
            if (time2.after(previousDate)) {
              long diff = time2.getTime() - previousDate.getTime();
              return addMilliseconds(time1, diff);
            }
            return addMilliseconds(time1, ONE_HOUR);
          }
}
