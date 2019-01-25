package cn.cloud.websocket.controller;

import cn.cloud.websocket.common.Constants;
import cn.cloud.websocket.common.SpringContextUtils;
import cn.cloud.websocket.model.User;
import cn.cloud.websocket.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试{@link SimpMessagingTemplate}类的基本用法
 *
 * @author zx
 * @date 2018/10/10
 * @since 1.0.0
 */
@Slf4j
@Controller
@RequestMapping("/message")
public class RedisMessageController {


    @Autowired
    private RedisService redisService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 拉取指定监听路径的未读的WebSocket消息
     *
     * @param destination 指定监听路径
     * @return java.util.Map
     */
    @PostMapping("/pullUnreadMessage")
    @ResponseBody
    public Map<String, Object> pullUnreadMessage(String destination) {
        Map<String, Object> result = new HashMap<>();
        try {
            HttpSession session = SpringContextUtils.getSession();
            //当前登录用户
            User loginUser = (User) session.getAttribute(Constants.SESSION_USER);

            //存储消息的Redis列表名
            String listKey = Constants.REDIS_UNREAD_MSG_PREFIX + loginUser.getUsername() + ":" + destination;
            //从Redis中拉取所有未读消息
            List<String> messageList = redisService.rangeList(listKey, 0, -1);

            result.put("code", "200");
            if (messageList.size() > 0) {
                //删除Redis中的这个未读消息列表
                redisService.delete(listKey);
                //将数据添加到返回集，供前台页面展示
                result.put("result", messageList);
            }
        } catch (Exception e) {
            result.put("code", "500");
            result.put("msg", e.getMessage());
        }
        return result;
    }

    @PostMapping("/send")
    @ResponseBody
    public String send(@RequestParam String destination, @RequestParam String data) {
        messagingTemplate.convertAndSend(destination, data);
        return data;
    }

}
