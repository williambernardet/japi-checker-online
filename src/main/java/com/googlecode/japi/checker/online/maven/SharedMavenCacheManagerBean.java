package com.googlecode.japi.checker.online.maven;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class SharedMavenCacheManagerBean implements FactoryBean<MavenCache>, InitializingBean {
    private MavenCache shared;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        this.shared = new MavenCache();
    }

    @Override
    public MavenCache getObject() throws Exception {
        return shared;
    }

    @Override
    public Class<?> getObjectType() {
        return MavenCache.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
		
} 