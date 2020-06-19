# cloud-center
边缘平台的数据云中心
云控制中心主要实现三个功能：

## 1. 边缘设备数据的记录功能

边缘设备会通过UDP通信方式向云中心的对应设备端口发送数据包，云中心需要将接收的数据存储到数据库（基本数据、环境数据）或者本地磁盘（视频文件），数据的类型主要包括以下三类：

1. 设备的基本数据（设备id、设备类型、设备所有者、设备电量、连接状态等）

![image-20200619154823454](https://note.youdao.com/yws/public/resource/a590917cb48dcdcd15365607a22b2b6c/xmlnote/C72E45020D4042849568D85D91C334FC/5825)

2. 设备采集的环境数据（时间、经纬度、温度、湿度、风速等）

![image-20200619154800978](https://note.youdao.com/yws/public/resource/a590917cb48dcdcd15365607a22b2b6c/xmlnote/899991AA820C45498BBA9485401DDD14/5827)

3. 视频数据（具体形式待确定：整个视频文件？图片？暂时用如下文件形式记录）

![image-20200619155023999](https://note.youdao.com/yws/public/resource/a590917cb48dcdcd15365607a22b2b6c/xmlnote/B2FE0059C32142CC8B2D10A48BD6C9A6/5829)

## 2. 配合前端的数据展示功能

主要是配合前端做一些数据接口，对已经记录的边缘设备数据进行查询处理

1. 登录注册

简单的登录注册、用户信息修改等功能，资料一搜一把

![登录](https://note.youdao.com/yws/public/resource/a590917cb48dcdcd15365607a22b2b6c/xmlnote/E73085EAFF034AB884250A2FAF91412C/5831)



![用户资料](https://note.youdao.com/yws/public/resource/a590917cb48dcdcd15365607a22b2b6c/xmlnote/6B886FE849AA400BB07BDA0F35507C15/5833)



2. 设备管理和历史数据展示

- 对设备基本信息的修改，添加删除设备等

![设备管理2](https://note.youdao.com/yws/public/resource/a590917cb48dcdcd15365607a22b2b6c/xmlnote/AC3285C3D9C14B2B804E54A54BBECF8D/5835)

- 根据前端传入的设备id和时间对已经记录的数据进行查询操作，包括对应的环境数据和==视频数据==

![历史数据](https://note.youdao.com/yws/public/resource/a590917cb48dcdcd15365607a22b2b6c/xmlnote/FFD5B411FC034633B71900653B5C958D/5837)

## 3. 单设备的实时控制

通过与设备建立TCP连接，对设备发送相应的指令数据达到控制的目的，在功能1中最好是维护一个当前设备状态的数据类（把最新的数据包中的数据作为实时数据展示）

![单设备管理](https://note.youdao.com/yws/public/resource/a590917cb48dcdcd15365607a22b2b6c/xmlnote/5774272484264B1E8A15451B66F849C8/5839)
