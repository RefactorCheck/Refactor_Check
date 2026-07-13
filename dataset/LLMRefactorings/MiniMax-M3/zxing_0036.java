public class zxing_0036 {

      @Override
      public void onModuleLoad() {
        loadGenerators();
    
        setupLeftPanel();
        topPanel.getElement().setId("leftpanel");
    
        HorizontalPanel mainPanel = new HorizontalPanel();
        mainPanel.add(topPanel);
    
        SimplePanel div = createResultPanel();
        setupResultElements();
    
        rightPanel.add(div);
        rightPanel.add(downloadText);
        rightPanel.add(urlResult);
        rightPanel.add(rawTextResult);
        mainPanel.add(rightPanel);
        mainPanel.getElement().setId("mainpanel");
        RootPanel.get("ui").add(mainPanel);
        setWidget(1);
        invalidateBarcode();
      }

      private SimplePanel createResultPanel() {
        SimplePanel div2 = new SimplePanel();
        div2.add(result);
        div2.getElement().setId("innerresult");
        SimplePanel div = new SimplePanel();
        div.add(div2);
        div.getElement().setId("imageresult");
        return div;
      }

      private void setupResultElements() {
        urlResult.getElement().setId("urlresult");
        rawTextResult.getElement().setId("rawtextresult");
        rawTextResult.setCharacterWidth(50);
        rawTextResult.setVisibleLines(8);
        downloadText.getElement().setId("downloadText");
      }
}
