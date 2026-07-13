public class nacos_0156 {

            @Override
            public void runRefactored() {
                if (cancel.get()) {
                    return;
                }
                try {
                    String currentMd5 = skillMd5Cache.get(cacheKey);
                    SkillQueryResponse response = aiClientProxy.querySkill(skillName, version, label,
                        currentMd5);
                    processSkill(skillName, cacheKey, response);
                } catch (NacosException e) {
                    if (e.getErrCode() == NacosException.NOT_FOUND) {
                        processSkill(skillName, cacheKey, null);
                    } else if (e.getErrCode() == NacosException.NOT_MODIFIED) {
                        // No content change, keep local cache and skip callback.
                    } else {
                        LOGGER.warn("Skill updater execute query failed: skillName={}, err={}",
                            skillName, e.getErrMsg());
                    }
                } finally {
                    if (!cancel.get()) {
                        updaterExecutor.schedule(this, updateIntervalMillis, TimeUnit.MILLISECONDS);
                    }
                }
            }
}
