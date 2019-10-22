# monkey

## usgae
1. adb push framework.jar /sdcard
   adb push monkey.jar /sdcard

2. adb shell CLASSPATH=/sdcard/monkey.jar:/sdcard/framework.jar exec app_process /system/bin tv.panda.test.monkey.Monkey -p {$packagename} --uiautomatormix --running-minutes {&time} -v -v

3. --uiautomatordfs 增加深度遍历算法
   --uiautomatormix 混合策略
   --uiautomatortroy 控件选择策略按max.xpath.selector配置的高低优先级来进行深度遍历


## reference
https://github.com/zhangzhao4444/Maxim