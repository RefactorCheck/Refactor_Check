@Override
        public static Map<K, Collection<V>> create(Object... elements)  {

          Set<K> keySet = new HashSet<>();
          List<Entry<K, V>> builder = new ArrayList<>();
          for (Object o : elements) {
            Entry<?, ?> entry = (Entry<?, ?>) o;
            // These come from Entry<K, Collection<V>>> objects somewhere.
            @SuppressWarnings("unchecked")
            K key = (K) entry.getKey();
            keySet.add(key);
            for (Object v : (Collection<?>) entry.getValue()) {
              // These come from Entry<K, Collection<V>>> objects somewhere.
              @SuppressWarnings("unchecked")
              V value = (V) v;
              builder.add(mapEntry(key, value));
            }
          }
          checkArgument(keySet.size() == elements.length, "Duplicate keys");
          return multimapGenerator.create(builder.toArray()).asMap();
        


        }
