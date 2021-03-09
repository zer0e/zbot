# zbot
一个基于mirai_api_http机器人框架。  
目前支持关键字回复和定时任务。  
目前处于alpha开发版本，尚不稳定。不定期更新。   

# about
我的本意是编写一个易开发插件的框架，由于console的插件开发上手难度较大，插件的编写得详细阅读源码才可，因此入门成了很大的困难。  
所以为了让群友都能开发简单的基于回复的插件，因此有了这个项目。  
为了开发简单，本项目没有直接依赖mirai-console而是依赖mirai_api_http插件来编写，所以需要自行启动mirai-console。  
如果不会构建mirai-console建议使用[miraiOK](https://github.com/LXY1226/MiraiOK)  
因为是基于关键词进行回复，所以插件编写只需要定义监听的关键词还有监听人或群组，并且定义处理方法即可。开发难度相对较小。

# quick start
请查看项目[wiki](https://github.com/zer0e/zbot/wiki/)

# TODO
~~咕咕咕~~
- [ ] 前端编写，目前只是简单启动连接线程与处理线程而已，没有前端控制。
- [ ] api扩展，目前只封装了mirai_api_http的部分api，并且只支持发送文本及图片消息，后续考虑丰富发送类型。
- [x] 定时插件的设计编写
- [ ] 动态加载插件
- [ ] 插件与配置外置
