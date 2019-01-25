package cn.cloud.websocket.utils;


import com.google.gson.Gson;

/**
 * JSON相关公共方法（通过Gson实现）
 *
 * @author zx
 * @date 2017/5/2
 * @since 1.0.0
 */
public class JsonUtils {

    /**
     * 将对象转化为json字符串
     *
     * @param source Java对象
     * @return java.lang.String
     */
    public static <K> String toJson(K source) {
        Gson gson = new Gson();
        return gson.toJson(source);
    }

    /**
     * 将json字符串还原为目标对象
     *
     * @param source json字符串
     * @return K
     */
    public static <T> T fromJson(String source, Class<T> clazz) {
        Gson gson = new Gson();
        return gson.fromJson(source, clazz);
    }

}
