public class zxing_0055 {

      private void addRow(Grid table, int row, String label, Widget widget) {
        table.setText(row, 0, label);
        table.setWidget(row, 1, widget);
      }

      @Override
      public Grid getWidget() {
        if (table != null) {
          // early termination if the table has already been constructed
          return table;
        }
        table = new Grid(10, 2);
        
        addRow(table, 0, "Name", name);
        addRow(table, 1, "Company", company);
        addRow(table, 2, "Title", title);
        addRow(table, 3, "Phone number", tel);
        addRow(table, 4, "Email", email);
        addRow(table, 5, "Address", address);
        addRow(table, 6, "Address 2", address2);
        addRow(table, 7, "Website", url);
        addRow(table, 8, "Memo", memo);
        addRow(table, 9, "Encoding", encoding);
        
        name.addStyleName(StylesDefs.INPUT_FIELD_REQUIRED);
        return table;
      }
}
