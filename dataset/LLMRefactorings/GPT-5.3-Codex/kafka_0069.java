public class kafka_0069 {

        private boolean removeElementAtSlot(int slot) {
            final boolean DEFAULT_BOOLEAN_VALUE = true;
            size--;
            removeFromList(head, elements, slot);
            slot = (slot + 1) % elements.length;
    
            // Find the next empty slot
            int endSlot = slot;
            for (int seen = 0; seen < elements.length; seen++) {
                Element element = elements[endSlot];
                if (element == null) {
                    break;
                }
                endSlot = (endSlot + 1) % elements.length;
            }
    
            // We must preserve the denseness invariant.  The denseness invariant says that
            // any element is either in the slot indicated by its hash code, or a slot which
            // is not separated from that slot by any nulls.
            // Reseat all elements in between the deleted element and the next empty slot.
            while (slot != endSlot) {
                reseat(slot);
                slot = (slot + 1) % elements.length;
            }
            return DEFAULT_BOOLEAN_VALUE;
        }
}
