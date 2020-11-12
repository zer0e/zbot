package com.github.zer0e.mbot.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.zer0e.mbot.config.Config;
import com.github.zer0e.mbot.utils.ConfigUtil;
import com.github.zer0e.mbot.utils.HttpUtil;
import lombok.Data;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Api {
    private static final Config config = ConfigUtil.getConfig();
    private static final String host = config.getMirai_http_api().getHost();
    private static final String port = config.getMirai_http_api().getPort();
    private static final String auth_key = config.getMirai_http_api().getAuthKey();
    private static final String bot_qq = config.getMirai_http_api().getBot_qq();
    private static final String http_schema = "http://";
    private static final String ws_schema = "ws://";
    private static final String base_url = http_schema + host + ":" + port;
    @Getter private static final String ws_base_url = ws_schema + host + ":" + port;
    private static final String about_url = "/about";
    private static final String auth_url = "/auth";
    private static final String verify_url = "/verify";
    private static final String release_url = "/release";
    @Getter private static final String ws_get_msg_url = "/message?sessionKey=";
    private static final String send_group_msg_url = "/sendGroupMessage";
    private static final String send_friend_msg_url = "/sendFriendMessage";
    private static final String get_group_list_url = "/groupList?sessionKey=";
    private static final String get_member_list_url = "/memberList?sessionKey={0}&target={1}";
    private static final Logger logger = LoggerFactory.getLogger(Api.class);

    private String session;

    public Api() {
        session = get_session();
    }

    public String get_session(){
        if (session != null && verify_session(session)){
            return session;
        }
        String data = "{" +
                "\"authKey\": \"" + auth_key +"\"" +
                "}";
        String url = base_url + auth_url;
        JSONObject obj = HttpUtil.post(url, data);
        if (obj != null && (int)obj.get("code") == 0){
            session = (String)obj.get("session");
            verify_session(session);
        }
        return session;
    }

    public boolean verify_session(String session){
        return verify_or_release_session(session, verify_url);
    }
    public boolean release_session(String session){
        return verify_or_release_session(session, release_url);
    }

    private boolean verify_or_release_session(String session, String input_url) {
        String url = base_url + input_url;
        String data = "{" +
                "\"sessionKey\":" + "\"" + session + "\"" + "," +
                "\"qq\":" + bot_qq +
                "}";
        JSONObject res = HttpUtil.post(url, data);
        if (res != null && (int)res.get("code") == 0){
            return true;
        }else{
            return false;
        }
    }


    public boolean send_plain_msg_to_group(String group, String text){
        text = text.replace("\"","\\\"");
        String data = "{" +
                "\"sessionKey\": \"" + session + "\",\n" +
                "\"target\": " + group + ",\n" +
                "\"messageChain\": [\n" +
                "        { \"type\": \"Plain\", \"text\": \"" + text + "\"}" +
                "]}";
        String url = base_url + send_group_msg_url;

        JSONObject res = HttpUtil.post(url, data);
        if (res != null && (int)res.get("code") == 0){
            return true;
        }else{
            return false;
        }

    }

    public List<String> get_group_list(){
        List<String> res = new ArrayList<>();
        String url = base_url + get_group_list_url + get_session();
        String result = HttpUtil.get_with_string(url);
        if (result == null){
            return res;
        }
        JSONArray jsonArray = JSONArray.parseArray(result);
        for (int i = 0; i < jsonArray.size(); i++){
            JSONObject obj = JSONObject.parseObject(jsonArray.get(i).toString());
            res.add(obj.getString("id"));
        }
        logger.debug("获取群组：" + res.toString());
        return res;
    }


    public static void main(String[] args) {
        Api api = new Api();
        api.get_group_list();
    }
}
