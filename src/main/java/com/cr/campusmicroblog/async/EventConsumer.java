package com.cr.campusmicroblog.async;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContextAware;

/**
 * @author cr
 * @date 2020-11-18 23:12
 */
public interface EventConsumer extends InitializingBean, ApplicationContextAware {
}
