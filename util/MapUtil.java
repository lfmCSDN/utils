package com.sunyard.frameworkset.plugin.tsp.manager.utils;

import com.sunyard.frameworkset.log.Logger;
import com.sunyard.frameworkset.log.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

/**
 * Write class comments here
 * <p/>
 * User: liumeng
 * Date: 2018/4/4 10:43
 * version $Id: MapUtil.java, v 0.1 Exp $
 */


public class MapUtil {
    private final static Logger LOGGER = LoggerFactory.getLogger(MapUtil.class);
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) {
        if (map == null)
            return null;
        Object obj =null;
        try {
            obj=beanClass.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                    continue;
                }

                field.setAccessible(true);
                field.set(obj, map.get(field.getName().toUpperCase()));
            }
            }catch (Exception e){
                LOGGER.error("map转换为Object失败");
            }

        return obj;
    }
}
