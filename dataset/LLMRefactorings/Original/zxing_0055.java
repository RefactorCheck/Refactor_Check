public class zxing_0055 {

      @Override
      public Grid getWidget() {
        if (table != null) {
          // early termination if the table has already been constructed
          return table;
        }
        table = new Grid(10, 2);
        
        table.setText(0, 0, "Name");
        table.setWidget(0, 1, name);
        table.setText(1, 0, "Company");
        table.setWidget(1, 1, company);
        table.setText(2, 0, "Title");
        table.setWidget(2, 1, title);
        table.setText(3, 0, "Phone number");
        table.setWidget(3, 1, tel);
        table.setText(4, 0, "Email");
        table.setWidget(4, 1, email);
        table.setText(5, 0, "Address");
        table.setWidget(5, 1, address);
        table.setText(6, 0, "Address 2");
        table.setWidget(6, 1, address2);
        table.setText(7, 0, "Website");
        table.setWidget(7, 1, url);
        table.setText(8, 0, "Memo");
        table.setWidget(8, 1, memo);
        table.setText(9, 0, "Encoding");
        table.setWidget(9, 1, encoding);
        
        name.addStyleName(StylesDefs.INPUT_FIELD_REQUIRED);
        return table;
      }
}
