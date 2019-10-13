# captcha-killer | burp验证码识别接口调用插件
## 0x01 插件简介
`captcha-killer`要解决的是让burp能用上各种验证码识别技术！

![主界面](./doc/captcha-killer.png)

**注意：`captcha-killer`本身无法识别验证码，它专注于对各种验证码识别接口的调用。任何识别技术，只需要开放web调用api接口就可以通过
`captcha-killer`来调用。**


## 0x02 插件编译
安装maven后，到项目目录执行如下命令：

```
mvn package
```

#### 0x03 使用小案例
以下小案例已经编写完成，每周5在微信公众号（回忆飘如雪）陆续更新

* [《captcha-killer使用步骤介绍》](./doc/Usage.md)
* 《captcha-killer调用tesseract-ocr识别验证码》[完成]
* 《captcha-killer调用完美识别验证码系统》[待更新]
* 《captcha-killer调用百度ocr识别验证码》[待更新][已完成]
* 《capatch-killer+机器学习识别验证码》[待更新]

## 0x04 同类项目
* [reCAPTCHA](https://github.com/bit4woo/reCAPTCHA)
* [实现一个简单的Burp验证码本地识别插件](https://www.freebuf.com/articles/web/168679.html)
* https://github.com/Releasel0ck/reCAPTCHA