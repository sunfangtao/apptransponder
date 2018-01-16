package com.sft.chain;

import org.apache.shiro.config.Ini;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.CollectionUtils;

public class ShiroPermissionFactory extends ShiroFilterFactoryBean {

    public static String definition;

    /**
     * 初始化设置过滤链
     */
    @Override
    public void setFilterChainDefinitions(String definitions) {
        definition = definitions;
        // 加载配置默认的过滤链
        Ini ini = new Ini();
        ini.load(definitions);
        Ini.Section section = ini.getSection("urls");
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection("");
        }

        // 上传图片需要APP用户认证
        section.put("/trans/pic", "user");
        section.put("/**", "anon");
        this.setFilterChainDefinitionMap(section);
    }

}  