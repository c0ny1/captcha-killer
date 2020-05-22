<h1 align="center">captcha-killer</h1>

<p align="center">
  <img title="portainer" src='https://img.shields.io/badge/version-0.1-brightgreen.svg' />
  <img title="portainer" src='https://img.shields.io/badge/java-1.7.*-yellow.svg' />
  <img title="portainer" src='https://img.shields.io/badge/license-MIT-red.svg' />
</p>

## 0x01 插件简介
`captcha-killer`要解决的问题是让burp能用上各种验证码识别技术！

**注意：**
1. 插件目前针对的图片型验证码，其他类型目前不支持。
2. `captcha-killer`本身无法识别验证码，它专注于对各种验证码识别接口的调用。

![主界面](./doc/captcha-killer.png)

## 0x02 插件编译
安装maven后，到项目目录执行如下命令：

```
mvn package
```

## 0x03 使用案例
以下小案例已经编写完成，每周五在微信公众号（[回忆飘如雪](https://mp.weixin.qq.com/s/I2r-lUddVHTmBRsg0J4nxw)）陆续更新

* [《使用burp插件captcha-killer识别图片验证码》](http://gv7.me/articles/2019/burp-captcha-killer-usage/)
* 《captcha-killer调用tesseract-ocr识别验证码》[待发布]
* 《captcha-killer调用完美识别验证码系统》[待发布]
* 《captcha-killer调用百度ocr识别验证码》[待发布]
* 《capatch-killer+机器学习识别验证码》[待发布]

## 0x04 注意事项
* 请求验证码的cookie要和爆破表单的cookie一致
* 必须单线程爆破，多线程会出现验证码失效问题

## 0x05 同类项目
* [reCAPTCHA](https://github.com/bit4woo/reCAPTCHA)
* [实现一个简单的Burp验证码本地识别插件](https://www.freebuf.com/articles/web/168679.html)
* https://github.com/Releasel0ck/reCAPTCHA