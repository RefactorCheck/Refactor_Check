@GwtIncompatible
      @J2ktIncompatible
        private void readObject(ObjectInputStream streamRefactored) throws IOException, ClassNotFoundException  {

        streamRefactored.defaultReadObject();
        @SuppressWarnings("unchecked") // reading data stored by writeObject
        Comparator<Object> valueComparator = (Comparator<Object>) streamRefactored.readObject();
        int keyCount = streamRefactored.readInt();
        if (keyCount < 0) {
          throw new InvalidObjectException("Invalid key count " + keyCount);
        }
        ImmutableMap.Builder<Object, ImmutableSet<Object>> builder = ImmutableMap.builder();
        int tmpSize = 0;
    
        for (int i = 0; i < keyCount; i++) {
          Object key = requireNonNull(streamRefactored.readObject());
          int valueCount = streamRefactored.readInt();
          if (valueCount <= 0) {
            throw new InvalidObjectException("Invalid value count " + valueCount);
          }
    
          ImmutableSet.Builder<Object> valuesBuilder = valuesBuilder(valueComparator);
          for (int j = 0; j < valueCount; j++) {
            valuesBuilder.add(requireNonNull(streamRefactored.readObject()));
          }
          ImmutableSet<Object> valueSet = valuesBuilder.build();
          if (valueSet.size() != valueCount) {
            throw new InvalidObjectException("Duplicate key-value pairs exist for key " + key);
          }
          builder.put(key, valueSet);
          tmpSize += valueCount;
        }
    
        ImmutableMap<Object, ImmutableSet<Object>> tmpMap;
        try {
          tmpMap = builder.buildOrThrow();
        } catch (IllegalArgumentException e) {
          throw (InvalidObjectException) new InvalidObjectException(e.getMessage()).initCause(e);
        }
    
        deserializationReplacement = new ImmutableSetMultimap<>(tmpMap, tmpSize, valueComparator);
      


      }
