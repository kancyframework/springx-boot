package com.kancy.jevel;

import java.util.Map;

/**
 * DynamicHandler
 *
 * @author huangchengkang
 * @date 2021/12/22 12:51
 */
public interface DynamicHandler<T>{

    /**
     * 处理
     *
     * @param context 上下文
     * @return {@link T}
     */
    T handle(Map<String,Object> context);

}
