package cn.cloud.websocket.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 自定义Spring工具类
 *
 * @author zx
 */
@Slf4j
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ConfigurableApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = (ConfigurableApplicationContext) context;
    }

    /**
     * 获取ApplicationContext对象
     *
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 停止应用程序
     */
    public static void close() {
        if (applicationContext != null) {
            applicationContext.close();
        }
    }

    /**
     * 根据bean的名称获取bean
     *
     * @param name
     * @return
     */
    public static Object getBeanByName(String name) {
        return applicationContext.getBean(name);
    }

    /**
     * 根据bean的class来查找对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBeanByClass(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    /**
     * 根据bean的class来查找所有的对象（包括子类）
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> getBeansByClass(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }

    /**
     * 获取HttpServletRequest
     *
     * @return
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attributes.getRequest();
    }

    /**
     * 获取HttpSession
     *
     * @return
     */
    public static HttpSession getSession() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attributes.getRequest().getSession();
    }

    /**
     * 获取完整的请求URL
     *
     * @return
     */
    public static String getRequestUrl() {
        return getRequestUrl(getRequest());
    }

    /**
     * 获取完整的请求URL
     *
     * @param request
     * @return
     */
    public static String getRequestUrl(HttpServletRequest request) {
        //当前请求路径
        String currentUrl = request.getRequestURL().toString();
        //请求参数
        String queryString = request.getQueryString();
        if (!StringUtils.isEmpty(queryString)) {
            currentUrl = currentUrl + "?" + queryString;
        }

        String result = "";
        try {
            result = URLEncoder.encode(currentUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.info("获取完整的请求URL错误：", e.getMessage());
        }

        return result;
    }

    /**
     * 获取请求的客户端IP
     *
     * @param request
     * @return
     */
    public static String getRequestIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

}
