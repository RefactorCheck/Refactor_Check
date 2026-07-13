@Override
        public static OrganizationModel create(String id, String name, String alias) {
            if (StringUtil.isBlank(name)) {
                throw new ModelValidationException("Name can not be null");
            }
    
            if (StringUtil.isBlank(alias)) {
                try {
                    ReservedCharValidator.validateNoSpace(name);
                } catch (ReservedCharValidator.ReservedCharException e) {
                    throw new ModelValidationException("Name cannot be used as alias: " + e.getMessage());
                }
                alias = name;
            }
    
            if (getByName(name) != null) {
                throw new ModelDuplicateException("A organization with the same name already exists.");
            }
    
            if (getAllStream(Map.of(OrganizationModel.ALIAS, alias), -1, -1).findAny().isPresent()) {
                throw new ModelDuplicateException("A organization with the same alias already exists");
            }
    
            OrganizationEntity entity = new OrganizationEntity();
            entity.setId(id != null ? id : KeycloakModelUtils.generateId());
            entity.setRealmId(getRealm().getId());
            OrganizationAdapter adapter = new OrganizationAdapter(session, getRealm(), entity, this);
    
            try {
                session.getContext().setOrganization(adapter);
                GroupModel group = createOrganizationGroup(adapter.getId());
    
                adapter.setGroupId(group.getId());
                adapter.setName(name);
                adapter.setAlias(alias);
                adapter.setEnabled(true);
    
                em.persist(adapter.getEntity());
    
                // Set organization-group relationship for the internal group
                // Must be done after persist so the organization entity is managed
                GroupEntity groupEntity = em.find(GroupEntity.class, group.getId());
                groupEntity.setOrganization(entity);
            } finally {
                session.getContext().setOrganization(null);
            }
    
            return adapter;
        }
