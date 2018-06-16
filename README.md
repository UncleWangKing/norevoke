# norevike
一个可以备份文件小工具 创作动机是备份QQ撤回的消息<br />
工作原理 <br />
    监听指定目录的文件修改事件，任何指定时间内(默认130秒--超过可撤回的限制120秒)创建的文件备份到jar包自创的文件夹内，并删除jar包中超时的文件<br />
使用介绍 <br />
    1.修改Main函数中的变量 SOURCE_FILE_PATH 为要监控的文件路径<br />
    ![Image text](https://github.com/UncleWangKing/norevoke/blob/master/img-folder/step1.png)<br />
    2.使用maven打包 -- 请使用带jar-with-dependencies后缀的jar 否则找不到依赖<br />
    3.放在任意空文件夹下 java -jar ---后台运行 请使用javaw -jar
