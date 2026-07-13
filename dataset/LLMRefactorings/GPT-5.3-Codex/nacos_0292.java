public class nacos_0292 {


        @Since("3.0.0")
        @GetMapping
        @Secured(resource = Constants.CAPACITY_CONTROLLER_V3_ADMIN_PATH, action = ActionTypes.READ,
            signType = SignType.CONFIG, apiType = ApiType.ADMIN_API) public Result<Capacity> getCapacity(@RequestParam(required = false) String groupName, @RequestParam(required = false) String namespaceId) throws NacosApiException {
        final String EXTRACTED_CONSTANT = "At least one of the parameters (groupName or namespaceId) must be provided";
            if (StringUtils.isBlank(groupName) && StringUtils.isBlank(namespaceId)) {
                throw new NacosApiException(HttpStatus.BAD_REQUEST.value(), ErrorCode.PARAMETER_MISSING,
                    EXTRACTED_CONSTANT);
            }
            
            try {
                Capacity capacity = capacityService.getCapacityWithDefault(groupName, namespaceId);
                if (capacity == null) {
                    LOGGER.warn(
                        "[getCapacity] capacity not exist，need init groupName: {}, namespaceId: {}",
                        groupName,
                        namespaceId);
                    capacityService.initCapacity(groupName, namespaceId);
                    capacity = capacityService.getCapacityWithDefault(groupName, namespaceId);
                }
                return Result.success(capacity);
            } catch (Exception e) {
                LOGGER.error(
                    "[getCapacity] Failed to fetch capacity for groupName: {}, namespaceId: {}",
                    groupName,
                    namespaceId, e);
                return Result.failure(ErrorCode.SERVER_ERROR.getCode(), e.getMessage(), null);
            }
        
            }
}
