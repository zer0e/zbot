# zbot
一个基于mirai_http_api和关键字回复的机器人框架。  
目前处于alpha开发版本，尚不稳定。不定期更新。   

# about
我的本意是编写一个易开发插件的框架，由于console的插件开发上手难度较大，插件的编写得详细阅读源码才可，因此入门成了很大的困难。  
所以为了让群友都能开发简单的基于回复的插件，因此有了这个项目。  
为了开发简单，本项目没有依赖mirai而是依赖mirai_http_api插件来编写，所以需要自行启动mirai-console。  
如果不会构建mirai-console建议使用[miraiOK](https://github.com/LXY1226/MiraiOK)  
因为是基于关键词进行回复，所以插件编写只需要定义监听的关键词还有监听人或群组，并且定义处理方法即可。开发难度相对较小。

# quick start
首先是api，在api中我封装了一些方法供插件使用。  
主要有：  
- 发送群组文本消息  boolean send_plain_msg_to_group(String group_id, String text)
- 发送好友消息 boolean send_plain_msg_to_friend(String friend_id, String text)
- 发送临时消息 boolean send_plain_msg_to_tmp_friend(String group_id, String friend_id, String text)
- 获取群组列表 List<String> get_group_list()
- 获取某个群组的成员列表 List<String> get_group_member(String group)

知道以上的几个api就可以开始编写插件了。  
定义一个关键词**群组**插件，只需在plugins目录下新建一个插件，继承于KeywordPlugin并且实现GroupPlugin接口即可。  
以下是一个测试插件。在原代码中也提供了两个插件参考。  
```java
public class TestPlugin extends KeywordPlugin implements GroupPlugin{
    // 插件需要回复，需要用到api
    private Api api;
    private static Logger logger = LoggerFactory.getLogger(TestPlugin.class);
    public TestPlugin() {
        // 在构造函数中初始化关键词
        init();
    }

    @Override
    protected void init() {
        // 如果是群插件只需向group_words_set和group_ids_set添加关键词和监听群
        // 初始化关键字与监听的群组
        // 监听群组中的*代表所有群组
        this.group_words_set.add("test");
        this.group_ids_set.add("*");

        // 初始化API避免大量申请session
        api = new Api();
    }
    // 实现GroupPlugin中的方法即可在收到该关键词时调用这个函数
    @Override
    public int callback(GroupMsg msg) {
        boolean is_send = api.send_plain_msg_to_group(msg.getSender_group(), "收到test");
        // 目前来说返回值并不影响框架的整体运行，但还是保留返回值
        // 如果需要查看是否回复成功可以自行日志打印
        logger.info("消息回复是否成功：" + is_send);
        return 0;
    }
}
```
随后启动mirai-console并且开启mirai_http_api插件，登录机器人账号。  
在resources/config.yaml中设置mirai_http_api的各项参数，并添加需要注册的插件类名，导入pom文件后启动Run即可。

# TODO
~~咕咕咕~~
- [ ] 前端编写，目前只是简单启动连接线程与处理线程而已，没有前端控制。
- [ ] api扩展，目前只封装了mirai_http_api的部分api，并且只支持发送文本消息，后续考虑丰富发送类型。
- [ ] 定时插件的设计编写