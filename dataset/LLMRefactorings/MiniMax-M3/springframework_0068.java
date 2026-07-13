public class springframework_0068 {

    protected void parseListenerConfiguration(Element ele, ParserContext parserContext, MutablePropertyValues configValues) {
        String destination = ele.getAttribute(DESTINATION_ATTRIBUTE);
        if (!StringUtils.hasText(destination)) {
            parserContext.getReaderContext().error(
                    "Listener 'destination' attribute contains empty value.", ele);
        }
        configValues.add("destinationName", destination);

        addOptionalAttribute(ele, parserContext, configValues, SUBSCRIPTION_ATTRIBUTE, "subscriptionName", "subscription");
        addOptionalAttribute(ele, parserContext, configValues, SELECTOR_ATTRIBUTE, "messageSelector", "selector");
        addOptionalAttribute(ele, parserContext, configValues, CONCURRENCY_ATTRIBUTE, "concurrency", "concurrency");
    }

    private void addOptionalAttribute(Element ele, ParserContext parserContext, MutablePropertyValues configValues,
                                      String attributeName, String propertyName, String label) {
        if (ele.hasAttribute(attributeName)) {
            String value = ele.getAttribute(attributeName);
            if (!StringUtils.hasText(value)) {
                parserContext.getReaderContext().error(
                        "Listener '" + label + "' attribute contains empty value.", ele);
            }
            configValues.add(propertyName, value);
        }
    }
}
