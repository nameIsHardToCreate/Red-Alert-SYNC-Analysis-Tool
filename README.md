这个工具是为了帮助《红色警戒:世界战火》作者快速定位RE问题，修Bug写的（催更)，我不知道其他版本的红警作者注意的地方与这个是不是一样，这个工具截取的片段是从开始到Delay，列出不同的行，并标红变色。
![image](https://github.com/user-attachments/assets/ad5880a7-059b-4635-bf6e-2860ff13afc4)
下面是结果图
![image](https://github.com/user-attachments/assets/f7828ac0-53bd-48e5-9a83-bd50f03a6a5b)


demo文件夹是该工具后端源码，red文件夹是该工具的前端源码。
demo使用的是Java 11，red使用的是Vue3。
源码很乱，很差劲，因为这是我提供思路让AI写的，勿喷。
nginx文件夹里面已经做好了配置，理论上说，本地有java 11 环境就可以直接使用。


使用方法：
下载nginx文件夹后里面有一个start_services.bat，双击它就可以启用了。
然后打开浏览器(我用的是EDGE)，搜索栏输入localhost,回车就行。
如果前端页面无法正常打开，说明有程序正在使用80端口，可以搜索如何查看并杀死80端口的进程。
进入页面后就可以上传你的SYNC文件了。（必须是同一局的SYNC文件，否则会报错)。


This tool is designed to help the authors of Command & Conquer: Red Alert – World War quickly locate RE issues and write bug fixes (to encourage updates). I’m not sure if other versions of Red Alert have the same concerns as this one. The segments captured by this tool are from the beginning up to Delay.List different lines and highlight them in red
The second image is the result image.

The demo folder contains the backend source code for the tool, while the red folder contains the frontend source code. The demo uses Java 11, and the red uses Vue 3. The source code is quite messy and not very good because I provided the ideas for the AI to write it, so please don’t criticize too much.

Usage Instructions:
Download the nginx folder; it contains a start_services.bat file. Double-click it to start the services.

Then, open your browser (I used Edge) and enter localhost in the address bar, then hit Enter.

If the frontend page doesn’t load properly, it means another program is using port 80. You can search for how to check and kill processes using port 80.

Once you access the page, you can upload your SYNC files (they must be from the same session, or an error will occur).
