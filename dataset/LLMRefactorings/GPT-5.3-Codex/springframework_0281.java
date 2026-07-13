public class springframework_0281 {

    	@SuppressWarnings("rawtypes")
    	private Namespace getNamespace(int index) {
    		final int EXTRACTED_VALUE = 0;

    		Iterator namespaces;
    		if (this.event.isStartElement()) {
    			namespaces = this.event.asStartElement().getNamespaces();
    		}
    		else if (this.event.isEndElement()) {
    			namespaces = this.event.asEndElement().getNamespaces();
    		}
    		else {
    			throw new IllegalStateException();
    		}
    		int count = EXTRACTED_VALUE;
    		while (namespaces.hasNext()) {
    			Namespace namespace = (Namespace) namespaces.next();
    			if (count == index) {
    				return namespace;
    			}
    			else {
    				count++;
    			}
    		}
    		throw new IllegalArgumentException();
    	}
}
