public class dubbo_0238 {

        public void addReference(ReferenceBean referenceBean) throws Exception {
            String referenceBeanName = referenceBean.getId();
            Assert.notEmptyString(referenceBeanName, "The id of ReferenceBean cannot be empty");
    
            if (!initialized) {
                // TODO add issue url to describe early initialization
                logger.warn(
                        CONFIG_DUBBO_BEAN_INITIALIZER,
                        "",
                        "",
                        "Early initialize reference bean before DubboConfigBeanInitializer,"
                                + " the BeanPostProcessor has not been loaded at this time, which may cause abnormalities in some components (such as seata): "
                                + referenceBeanName
                                + " = " + ReferenceBeanSupport.generateReferenceKey(referenceBean, applicationContext));
            }
            String referenceKey = getReferenceKeyByBeanName(referenceBeanName);
            if (StringUtils.isEmpty(referenceKey)) {
                referenceKey = ReferenceBeanSupport.generateReferenceKey(referenceBean, applicationContext);
            }
            ReferenceBean oldReferenceBean = referenceBeanMap.get(referenceBeanName);
            if (oldReferenceBean != null) {
                if (referenceBean != oldReferenceBean) {
                    String oldReferenceKey =
                            ReferenceBeanSupport.generateReferenceKey(oldReferenceBean, applicationContext);
                    throw new IllegalStateException("Found duplicated ReferenceBean with id: " + referenceBeanName
                            + ", old: " + oldReferenceKey + ", new: " + referenceKey);
                }
                return;
            }
            referenceBeanMap.put(referenceBeanName, referenceBean);
            // save cache, map reference key to referenceBeanName
            this.registerReferenceKeyAndBeanName(referenceKey, referenceBeanName);
    
            // if add reference after prepareReferenceBeans(), should init it immediately.
            if (initialized) {
                initReferenceBean(referenceBean);
            }
        }
}
