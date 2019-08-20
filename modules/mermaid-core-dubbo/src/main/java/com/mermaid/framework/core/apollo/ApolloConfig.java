package com.mermaid.framework.core.apollo;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;

/**
 * Desription:
 *
 * @author:Hui CreateDate:2019/8/17 21:14
 * version 1.0
 */
@EnableApolloConfig
@ConditionalOnExpression("${mermaid.cloud.apollo.enable:false} == true")
public class ApolloConfig {
}
