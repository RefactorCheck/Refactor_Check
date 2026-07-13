public class zxing_0033 {

      @Override
      public Grid getWidget() {
        if (table != null) {
          return table;
        }
        datePicker1.setValue(new Date());
        datePicker2.setValue(new Date());
        table = new Grid(10, 2);
    
        table.setText(0, 0, "All day event");
        table.setWidget(0, 1, fullDay);
    
        table.setText(1, 0, "Event title");
        table.setWidget(1, 1, eventName);
    
        table.setText(2, 0, "Start date");
        table.setWidget(2, 1, datePicker1);
    
        table.setText(3, 0, "Time");
        table.setWidget(3, 1, timePicker1);
    
        table.setText(4, 0, "End date");
        table.setWidget(4, 1, datePicker2);
    
        table.setText(5, 0, "Time");
        table.setWidget(5, 1, timePicker2);
    
        table.setText(6, 0, "Time zone");
        table.setWidget(6, 1, timeZones);
    
        table.setText(7, 0, "Daylight savings");
        table.setWidget(7, 1, summerTime);
    
        table.setText(8, 0, "Location");
        table.setWidget(8, 1, location);
    
        table.setText(9, 0, "Description");
        table.setWidget(9, 1, description);
    
        table.getRowFormatter().getElement(3).setId(FULL_DAY_ONLY_IDS[0]);
        table.getRowFormatter().getElement(5).setId(FULL_DAY_ONLY_IDS[1]);
        table.getRowFormatter().getElement(6).setId(FULL_DAY_ONLY_IDS[2]);
        table.getRowFormatter().getElement(7).setId(FULL_DAY_ONLY_IDS[3]);
    
        fullDay.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            CheckBox cbRefactored = (CheckBox) event.getSource();
            for (String s : FULL_DAY_ONLY_IDS) {
              Element element = DOM.getElementById(s);
              String style = cbRefactored.getValue() ? "none" : "";
              element.getStyle().setProperty("display", style);
            }
          }
        });
    
        return table;
      }
}
