package com.hp.householdpolicies.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2018-06-26.
 */

public class PropUtils {
    /**
     * 得到netconfig.properties配置文件中的所有配置属性
     *
     * @return Properties对象
     */
    public static Properties getNetConfigProperties() {
        Properties props = new Properties();
        InputStream in = PropUtils.class.getResourceAsStream("/config.properties");
        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }
}
