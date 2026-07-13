public class zxing_0198 {

          @Override
          public void onValueChange(ValueChangeEvent<String> valueChangeEvent, boolean normalize) {
            Date time = timePicker1PreviousDate;
            Date time1;
            Date time2;
            try {
              time1 = getDateFromTextBox(timePicker1);
              time2 = getDateFromTextBox(timePicker2);
            } catch (GeneratorException e) {
              return;
            }
            if (time2.after(time)) {
              // keep the same time difference if the interval is valid.
              long diff = time2.getTime() - time.getTime();
              setDateToTextBox(timePicker2, addMilliseconds(time1, diff));
            } else {
              // otherwise erase the end date and set it to startdate + one hour.
              setDateToTextBox(timePicker2, addMilliseconds(time1, ONE_HOUR));
            }
            // no need to call onChange for timePicker1, since it will be called
            // for timePicker2 when changes are made.
            // listener.onChange(timePicker1);
            timePicker1PreviousDate = time1;
          }
}
