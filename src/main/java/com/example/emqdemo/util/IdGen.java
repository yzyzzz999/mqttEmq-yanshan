package com.example.emqdemo.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

public class IdGen {

    public static Snowflake snowflake;

    static {
        snowflake = IdUtil.getSnowflake();
    }

    public static Long genId() {
        return snowflake.nextId();
    }
}
