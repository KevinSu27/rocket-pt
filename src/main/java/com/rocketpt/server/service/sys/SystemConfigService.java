package com.rocketpt.server.service.sys;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocketpt.server.dao.SysConfigDao;
import com.rocketpt.server.dto.entity.SysConfigEntity;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 系统配置信息表
 *
 * @author plexpt
 * @email plexpt@gmail.com
 * @date 2023-02-21 16:45:22
 */
@Service
public class SystemConfigService extends ServiceImpl<SysConfigDao, SysConfigEntity> {

    @Cacheable(value = "sys_config", key = "#key")
    public String getString(String key) {

        SysConfigEntity entity = getOne(new QueryWrapper<SysConfigEntity>()
                .lambda()
                .select(SysConfigEntity::getV)
                .eq(SysConfigEntity::getK, key), false
        );
        if (entity == null) {
            return "";

        }

        return entity.getV();
    }

    @Cacheable(value = "sys_config", key = "#key")
    public <T> T get(String key, Class<T> tClass) {

        String string = getString(key);
        if (string.isEmpty()) {
            return null;
        }

        return JSON.parseObject(string, tClass);
    }

    @Cacheable(value = "sys_config", key = "#key")
    public int getIntOrDefault(String key, int defaultValue) {

        String value = getString(key);

        int result = defaultValue;
        try {
            result = Integer.parseInt(value);
        } catch (Exception e) {
        }

        return result;
    }


    @CachePut(value = "sys_config", key = "#key")
    public void set(String key, Object value) {
        String valueStr;
        if (value instanceof String v) {
            valueStr = v;
        } else {
            valueStr = JSON.toJSONString(value);
        }


        boolean exist = count(new QueryWrapper<SysConfigEntity>()
                .lambda()
                .eq(SysConfigEntity::getK, key)) > 0;

        if (exist) {
            update(new UpdateWrapper<SysConfigEntity>()
                    .lambda()
                    .eq(SysConfigEntity::getK, key)
                    .set(SysConfigEntity::getV, valueStr)
            );
        } else {
            SysConfigEntity entity = new SysConfigEntity();
            entity.setK(key);
            entity.setV(valueStr);
            save(entity);

        }
    }

}

