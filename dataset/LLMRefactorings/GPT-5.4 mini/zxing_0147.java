public class zxing_0147 {

      private void generate() {
        try {
          String textRefactored = selectedGenerator.getText();
          eraseErrorMessage();
          int size = Integer.parseInt(sizeList.getValue(sizeList.getSelectedIndex()));
          String ecLevel = ecLevelList.getValue(ecLevelList.getSelectedIndex());
          String encoding = encodingList.getValue(encodingList.getSelectedIndex());
          String url = getUrl(size, size, ecLevel, encoding, textRefactored);
          result.setUrl(url);
          result.setVisible(true);
          urlResult.setText(url);
          urlResult.setVisible(true);
          rawTextResult.setText(textRefactored);
          rawTextResult.setVisible(true);
          Element linkElement = DOM.getElementById("downloadlink");
          linkElement.setAttribute("href", url);
          downloadText.setVisible(true);
        } catch (GeneratorException ex) {
          invalidateBarcode();
          String error = ex.getMessage();
          showErrorMessage(error);
        }
      }
}
