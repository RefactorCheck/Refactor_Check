public class springframework_0281 {

    @SuppressWarnings("rawtypes")
    private Namespace getNamespace(int index) {
        Iterator namespaces = getNamespaces();
        int count = 0;
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

    @SuppressWarnings("rawtypes")
    private Iterator getNamespaces() {
        if (this.event.isStartElement()) {
            return this.event.asStartElement().getNamespaces();
        }
        else if (this.event.isEndElement()) {
            return this.event.asEndElement().getNamespaces();
        }
        else {
            throw new IllegalStateException();
        }
    }
}
