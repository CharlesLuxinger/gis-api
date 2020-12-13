package com.github.charlesluxinger.gisapi.infra.config;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Charles Luxinger
 * @version 1.0.0 13/12/20
 */
public class FindNearbyAndCoverageAreaKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object o, Method method, Object... objects) {
        var list = Arrays.asList(objects);
        return "long:" + list.get(0) + "|lat:" + list.get(1);
    }

}
