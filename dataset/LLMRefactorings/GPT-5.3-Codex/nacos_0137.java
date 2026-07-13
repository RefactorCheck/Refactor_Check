public class nacos_0137 {


        @Override
        public ResponseEntity<byte[]> exportConfig(String dataId, String group, String namespaceId, String appName, List<Long> ids) throws Exception {
            List<ConfigAllInfo> dataList =
                configInfoPersistService.findAllConfigInfo4Export(dataId, group, namespaceId,
                    appName, ids);
            List<ZipUtils.ZipItem> zipItemList = new ArrayList<>();
            List<ConfigMetadata.ConfigExportItem> configMetadataItems = new ArrayList<>();
            for (ConfigAllInfo ci : dataList) {
                ConfigMetadata.ConfigExportItem configMetadataItem =
                    new ConfigMetadata.ConfigExportItem();
                configMetadataItem.setAppName(ci.getAppName());
                configMetadataItem.setDataId(ci.getDataId());
                configMetadataItem.setDesc(ci.getDesc());
                configMetadataItem.setGroup(ci.getGroup());
                configMetadataItem.setType(ci.getType());
                configMetadataItems.add(configMetadataItem);
                Pair<String, String> pair =
                    EncryptionHandler.decryptHandler(ci.getDataId(), ci.getEncryptedDataKey(),
                        ci.getContent());
                String itemName =
                    ci.getGroup() + Constants.CONFIG_EXPORT_ITEM_FILE_SEPARATOR + ci.getDataId();
                zipItemList.add(new ZipUtils.ZipItem(itemName, pair.getSecond()));
            }
            ConfigMetadata configMetadata = new ConfigMetadata();
            configMetadata.setMetadata(configMetadataItems);
            zipItemList.add(
                new ZipUtils.ZipItem(Constants.CONFIG_EXPORT_METADATA_NEW,
                    YamlParserUtil.dumpObject(configMetadata)));
            HttpHeaders headers = new HttpHeaders();
            String fileName =
                EXPORT_CONFIG_FILE_NAME
                    + DateFormatUtils.format(new Date(), EXPORT_CONFIG_FILE_NAME_DATE_FORMAT)
                    + EXPORT_CONFIG_FILE_NAME_EXT;
            headers.add("Content-Disposition", "attachment;filename=" + fileName);
            final ResponseEntity<byte[]> extractedResult = new ResponseEntity<>(ZipUtils.zip(zipItemList), headers, HttpStatus.OK);
            return extractedResult;
        
        }
}
