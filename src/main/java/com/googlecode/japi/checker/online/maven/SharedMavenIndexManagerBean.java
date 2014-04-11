package com.googlecode.japi.checker.online.maven;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class SharedMavenIndexManagerBean implements FactoryBean<MavenIndex>, InitializingBean {
    private MavenIndex shared;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        this.shared = new MavenIndex();
    }

    @Override
    public MavenIndex getObject() throws Exception {
        return shared;
    }

    @Override
    public Class<?> getObjectType() {
        return MavenIndex.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
		
} 