public class springframework_0081 {

    	public static @Nullable Object resolveMultipartArgument(String name, MethodParameter parameter, HttpServletRequest request)
    			throws Exception {
    
    		MultipartHttpServletRequest multipartRequest =
    				WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
    		boolean isMultipart = (multipartRequest != null || isMultipartContent(request));
    
    		if (MultipartFile.class == parameter.getNestedParameterType()) {
    			if (!isMultipart) {
    				return null;
    			}
    			if (multipartRequest == null) {
    				multipartRequest = new StandardMultipartHttpServletRequest(request);
    			}
    			return multipartRequest.getFile(name);
    		}
    		else if (isMultipartFileCollection(parameter)) {
    			if (!isMultipart) {
    				return null;
    			}
    			if (multipartRequest == null) {
    				multipartRequest = new StandardMultipartHttpServletRequest(request);
    			}
    			List<MultipartFile> files = multipartRequest.getFiles(name);
    			return (!files.isEmpty() ? files : null);
    		}
    		else if (isMultipartFileArray(parameter)) {
    			if (!isMultipart) {
    				return null;
    			}
    			if (multipartRequest == null) {
    				multipartRequest = new StandardMultipartHttpServletRequest(request);
    			}
    			List<MultipartFile> files = multipartRequest.getFiles(name);
    			return (!files.isEmpty() ? files.toArray(new MultipartFile[0]) : null);
    		}
    		else if (Part.class == parameter.getNestedParameterType()) {
    			if (!isMultipart) {
    				return null;
    			}
    			return request.getPart(name);
    		}
    		else if (isPartCollection(parameter)) {
    			if (!isMultipart) {
    				return null;
    			}
    			List<Part> parts = resolvePartList(request, name);
    			return (!parts.isEmpty() ? parts : null);
    		}
    		else if (isPartArray(parameter)) {
    			if (!isMultipart) {
    				return null;
    			}
    			List<Part> parts = resolvePartList(request, name);
    			return (!parts.isEmpty() ? parts.toArray(new Part[0]) : null);
    		}
    		else {
    			return UNRESOLVABLE;
    		}
    	}
}
