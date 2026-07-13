public class springframework_0292 {

    		@Override
    		public @Nullable Method getWriteMethod() {
    			if (this.writeMethod == null && !this.candidateWriteMethods.isEmpty()) {
    				if (this.readMethod == null || this.candidateWriteMethods.size() == 1) {
    					this.writeMethod = this.candidateWriteMethods.get(0);
    				}
    				else {
    					this.writeMethod = findBestMatchingWriteMethod();
    				}
    			}
    			return this.writeMethod;
    		}

    		private Method findBestMatchingWriteMethod() {
    			Class<?> resolvedReadType =
    					ResolvableType.forMethodReturnType(this.readMethod, this.beanClass).toClass();
    			Method bestMatch = null;
    			for (Method method : this.candidateWriteMethods) {
    				// 1) Check for an exact match against the resolved types.
    				Class<?> resolvedWriteType =
    						ResolvableType.forMethodParameter(method, 0, this.beanClass).toClass();
    				if (resolvedReadType.equals(resolvedWriteType)) {
    					return method;
    				}

    				// 2) Check if the candidate write method's parameter type is compatible with
    				// the read method's return type.
    				Class<?> parameterType = method.getParameterTypes()[0];
    				if (this.readMethod.getReturnType().isAssignableFrom(parameterType)) {
    					// If we haven't yet found a compatible write method, or if the current
    					// candidate's parameter type is a subtype of the previous candidate's
    					// parameter type, track the current candidate as the write method.
    					if (bestMatch == null ||
    							bestMatch.getParameterTypes()[0].isAssignableFrom(parameterType)) {
    						bestMatch = method;
    						// We do not "break" here, since we need to compare the current candidate
    						// with all remaining candidates.
    					}
    				}
    			}
    			return bestMatch;
    		}
}
