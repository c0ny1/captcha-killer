# captcha-killer | burp验证码识别接口调用插件
## 0x01 插件简介
`captcha-killer`要解决的是让burp能用上各种验证码识别技术！

![主界面](./doc/captcha-killer.png)

**注意：`captcha-killer`本身无法识别验证码，它专注于对各种验证码识别接口的调用。任何识别技术，只需要开放web调用api接口就可以通过
`captcha-killer`来调用。**

## 0x02 使用步骤

#### 1.将获取验证码的数据包发送到插件
在burp中查找验证码请求的数据包，右`captcha-killer` -> `send to captcha panel`即可！


注意：获取验证码的cookie一定要和intruder发送的cookie相同！
#### 2. 配置识别验证码的接口调用数据包
设置好配置接口的url，和数据包。建议配置好了之后，保存为模版，方便下次直接通过右键`模板库`中快速设置。

|ID|标签|描述|
|:---:|:---|:---|
|1|`{IMG_RAW}`|代表验证码图片原二进制内容|
|2|`<urlencode></urlencode>`|对标签内的内容进行url编码|
|3|`<base64></base64>`|对标签内的内容进行base64编码|

#### 3. 设置用于匹配识别结果的规则
插件提供了4中方式进行匹配，可以根据具体情况选择合适的。

|ID|规则类型|描述|
|:---:|:---|:---|
|1|Repose data|这种规则用于匹配接口返回包内容直接是识别结果|
|2|Regular expression|正则表达式,适合比较复杂的匹配。比如接口返回包`{"coede":1,"result":"abcd"}`说明abcd是识别结果，我们可以编写规则为`result":"(.*?)"\}`|
|3|Define the start and end positions|定义开始和结束位置,使用上面的例子，可以编写规则`{"start":21,"end":25}`|
|4|Defines the start and end strings|定义开始和结束字符，使用上面的例子，可以编写规则为`{"start":"result\":\","end":"\"\}"}`|

规则内容，可以通过选中规则类型，在选中识别结果，右键标记为识别结果即可自动生成。若匹配不精确，可以人工微调规则。
#### 4. 在intruder中调用
配置好各项后，可以点击锁定对当前配置进行锁定，防止被修改导致爆破失败！

更多案例可以访问[《使用captcha-killer插件对抗图片验证码》](http://gv7.me)

## 0x03 同类项目
* [reCAPTCHA](https://github.com/bit4woo/reCAPTCHA)
* [实现一个简单的Burp验证码本地识别插件](https://www.freebuf.com/articles/web/168679.html)
* https://github.com/Releasel0ck/reCAPTCHA