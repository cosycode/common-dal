package com.github.cosycode.dal;

import com.github.cosycode.dal.source.DataSourceConfig;
import org.junit.Test;

/**
 * <b>Description : </b>
 *
 * @author CPF
 * @date 2020/12/15 17:11
 **/
public class DataSourceTest {

    @Test
    public void test() {
        DataSourceConfig config = new DataSourceConfig();
        config.setDriver("");
        config.setUrl("");
        config.setUser("");
        config.setPassword("");
        config.setInitialPoolSize(0);
        config.setMaxPoolSize(0);
        config.setMinPoolSize(0);
        config.setAcquireIncrement(0);


    }

}
