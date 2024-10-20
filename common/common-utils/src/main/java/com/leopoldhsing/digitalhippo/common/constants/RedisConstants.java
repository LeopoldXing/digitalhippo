package com.leopoldhsing.digitalhippo.common.constants;

import java.util.concurrent.TimeUnit;

public class RedisConstants {

    public static final String VERIFICATION_TOKEN_PREFIX = "user:";
    public static final String VERIFICATION_TOKEN_SUFFIX = "verification:";

    public static final String USER_PREFIX = "user:";
    public static final String ACCESS_TOKEN_SUFFIX = "token:";
    public static final String USERID_SUFFIX = "uid:";

    public static final String PRODUCT_PREFIX = "product:";
    public static final String CACHE_SUFFIX = "cache:";

    public static final String PRODUCT_BITMAP_KEY = "bitmap:product:";

    public static final String LOCK_PREFIX = "lock:";
    public static final Long DEFAULT_TTL = 1L;
    public static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.HOURS;

    public static final Long ACCESS_TOKEN_VALID_MINUTES = 60L;

}
