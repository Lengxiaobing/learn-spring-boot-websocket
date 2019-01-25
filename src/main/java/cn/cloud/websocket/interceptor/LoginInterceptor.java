package cn.cloud.websocket.interceptor;

import cn.cloud.websocket.common.Constants;
import cn.cloud.websocket.common.SpringContextUtils;
import cn.cloud.websocket.model.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录拦截器
 *
 * @author zx
 * @date 2018/7/26
 * @since 1.0.0
 */
public class LoginInterceptor implements HandlerInterceptor {

    /**
     * 检查是否已经登录
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        //获取session中存储的token
        User user = (User) session.getAttribute(Constants.SESSION_USER);

        if (user != null) {
            return true;
        } else {
            //如果token不存在，则跳转到登录页面
            response.sendRedirect(request.getContextPath() + "/login?redirectUri=" + SpringContextUtils.getRequestUrl(request));
            return false;
        }
    }
}
