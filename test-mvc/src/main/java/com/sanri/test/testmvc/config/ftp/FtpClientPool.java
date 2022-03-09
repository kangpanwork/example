package com.sanri.test.testmvc.config.ftp;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class FtpClientPool extends GenericObjectPool<FtpClientExtend> {

    public FtpClientPool(PooledObjectFactory<FtpClientExtend> factory) {
        super(factory);
    }

    public FtpClientPool(PooledObjectFactory<FtpClientExtend> factory, GenericObjectPoolConfig config) {
        super(factory, config);
    }
}
