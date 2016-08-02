# hotkey
## 简化markdown写作中的贴图流程-windows+java版
[原文 mac+python版本](http://www.jianshu.com/p/7bd4e6ed99be) 

主要思路同原文一致，只是实现方法变为AutoHotKey+Java。

## 达到效果

在编辑markdown时需要插入截图，步骤如下：
1. 通过工具截图（比如qq）
2. 触发指定按键（比如：Ctrl+j）
3. 在Markdown中ctrl+v

内部过程：
通过截图工具截图后，图片是保存在windows的剪切板中的，通过AutoHotKey监听按键，触发按键时启动脚本运行Java程序，Java程序将剪切板中的图片上传到指定图床（我这里用的七牛云存储），然后得到该图片在云平台的url，将url加工后（构造成markdown识别的图片url）复制到剪切板，然后ctrl+v即可。

autoHotKey脚本：
```
^j::
   Run javaw -jar hotkey-0.0.1-jar-with-dependencies.jar
Return
```
源程序[hotkey](https://github.com/TopGunViper/hotkey)

```
git clone https://github.com/TopGunViper/hotkey.git

mvn package
```

用到工具：

[AutoHotKey](https://autohotkey.com/)

[七牛java SDK](http://o9gnz92z5.bkt.clouddn.com/code/v7/sdk/java.html)