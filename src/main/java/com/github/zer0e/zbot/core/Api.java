package com.github.zer0e.zbot.core;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.zer0e.zbot.config.Config;
import com.github.zer0e.zbot.utils.ConfigUtil;
import com.github.zer0e.zbot.utils.HttpUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
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
    private static final String send_temp_msg_url = "/sendTempMessage";
    private static final String get_group_list_url = "/groupList?sessionKey=";
    private static final String get_member_list_url = "/memberList?sessionKey={0}&target={1}";
    private static final Logger logger = LoggerFactory.getLogger(Api.class);
    private static final Api INSTANCE = new Api();

    private String session;

    private Api(){
        session = get_session();
    }

    public static Api getApi(){
        return INSTANCE;
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


    public boolean send_plain_msg_to_group(String group_id, String text){
        return send_plain_msg_to_friend_or_group(send_group_msg_url,group_id, text);
    }

    public boolean send_plain_msg_to_friend(String friend_id, String text){
        return send_plain_msg_to_friend_or_group(send_friend_msg_url, friend_id, text);
    }

    private boolean send_plain_msg_to_friend_or_group(String query_url, String target_id, String text) {
        text = text.replace("\"","\\\"");
        String data = "{" +
                "\"sessionKey\": \"" + session + "\",\n" +
                "\"target\": " + target_id + ",\n" +
                "\"messageChain\": [\n" +
                "        { \"type\": \"Plain\", \"text\": \"" + text + "\"}" +
                "]}";
        String url = base_url + query_url;
        JSONObject res = HttpUtil.post(url, data);
        if (res != null && (int)res.get("code") == 0){
            return true;
        }else{
            return false;
        }
    }

    public boolean send_plain_msg_to_tmp_friend(String group_id, String friend_id, String text){
        text = text.replace("\"","\\\"");
        String data = "{" +
                "\"sessionKey\": \"" + session + "\",\n" +
                "\"qq\": " + friend_id + ",\n" +
                "\"group\": " + group_id + ",\n" +
                "\"messageChain\": [\n" +
                "        { \"type\": \"Plain\", \"text\": \"" + text + "\"}" +
                "]}";
        String url = base_url + send_temp_msg_url;
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

    public List<String> get_group_member(String group){
        String url = MessageFormat.format(base_url + get_member_list_url,get_session(),group);
        String result = HttpUtil.get_with_string(url);
        if (result == null){
            return null;
        }
        JSONArray array = JSONArray.parseArray(result);
        List<String> res = new ArrayList<> ();
        for (int i = 0; i < array.size(); i++){
            res.add(array.get(i).toString());
        }
        logger.debug("获取成员列表：" + res.toString());
        return res;
    }



    public static void main(String[] args) {
        Api api = new Api();
        api.get_group_member("1234");
    }
}
