public class zxing_0033 {

      @Override
      public Grid getWidget() {
        if (table != null) {
          return table;
        }
        datePicker1.setValue(new Date());
        datePicker2.setValue(new Date());
        table = new Grid(10, 2);
    
        addRow(0, "All day event", fullDay);
        addRow(1, "Event title", eventName);
        addRow(2, "Start date", datePicker1);
        addRow(3, "Time", timePicker1);
        addRow(4, "End date", datePicker2);
        addRow(5, "Time", timePicker2);
        addRow(6, "Time zone", timeZones);
        addRow(7, "Daylight savings", summerTime);
        addRow(8, "Location", location);
        addRow(9, "Description", description);
    
        table.getRowFormatter().getElement(3).setId(FULL_DAY_ONLY_IDS[0]);
        table.getRowFormatter().getElement(5).setId(FULL_DAY_ONLY_IDS[1]);
        table.getRowFormatter().getElement(6).setId(FULL_DAY_ONLY_IDS[2]);
        table.getRowFormatter().getElement(7).setId(FULL_DAY_ONLY_IDS[3]);
    
        fullDay.addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            CheckBox cb = (CheckBox) event.getSource();
            for (String s : FULL_DAY_ONLY_IDS) {
              Element element = DOM.getElementById(s);
              String style = cb.getValue() ? "none" : "";
              element.getStyle().setProperty("display", style);
            }
          }
        });
    
        return table;
      }
    
      private void addRow(int row, String label, Widget widget) {
        table.setText(row, 0, label);
        table.setWidget(row, 1, widget);
      }
}
