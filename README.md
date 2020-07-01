# miaosha
1.电商秒杀项目简介
       :本项目是慕课网上的秒杀项目业务逻辑的基础版，后面还要加上性能优化，如项目中后期还针对高并发情况
       需要实现了系统缓存、降级和限流和支付通用功能等。本系统是使用SpringBoot开发的限时抢购秒杀系统，
       实现基本的登录、查看商品列表、秒杀、下单及其它异常的处理模板等功能。

- 用户首先需要进行注册账号，通过随机数生成的校验码对手机账号进行校验，并将用户输入的密码进行加密，
将密码和个人账号进行分表建库，注册完则需要进行登录，在登入页面未找到用户信息需重新注册，完成用户登录校验。
- 登入账号进去会显示当前商品列表页，点击某个商品会进入商品详情页，它会显示各商品的名称、商品信息、商品图片信息、价格、销量、库存
以及是否是秒杀商品，若是正在秒杀的商品会有秒杀结束倒计时，若是未开始的秒杀商品会有活动开始倒计时，秒杀的商品价格也会跟着变。
根据自己需要的商品可以进行下单，一般秒杀的商品以秒杀价格进行结算，普通商品以平时挂牌的价格结算，完成了基本的下单功能。
2.后台开发环境
开发环境：IDEA+Maven+SpringBoot+MyBatis+Mysql+Git
项目结构：

- 技术点：
1）JSR303自定义参数验证
使用JSR303自定义校验器，实现对用户账号、密码的验证，使得验证逻辑从业务代码中脱离出来。

2）全局异常统一处理
使用Springboot中的@ExceptionHandler来解决未被controller层吸收的exception，通过拦截所有异常，对各种异常进行相应的处理，
当遇到异常就逐层上抛，一直抛到最终由一个统一的、专门负责异常处理的地方处理，这有利于对异常的维护。

3）通用返回模型
为了使前端获得正确的json数据，且获得后端服务的状态，采用定义一个CommonReturnType类，通过定义静态的通用创建方法create，该创建方法定义服务状态参数，
然后使用重载的方式来获得网络服务的状态和需要展现给前端的json数据。并定义相关错误的错误码，使得快速定位错误出现地方。

4）页面静态化
对商品详情和订单详情进行页面静态化处理，页面是存在html，动态数据是通过接口从服务端获取，实现前后端分离，
静态页面无需连接数据库打开速度较动态页面会有明显提高

3.开发流程：
  3.1. 采用Maven构建项目，选择org.apache.maven.archetypes.maven-archetype-quickstart，以这样的方式是以一个jar的方式进行输出，
后期可以直接打包导入依赖进行使用。指定src-main-java文件夹为Mark Directory的Sources Root，表示是java的源代码；
src-main-test文件夹为Mark Directory的Test Sources Root，然后在main下新建资源文件的目录resourcesMark Directory的 Resources Root；
  
  3.2.接下来就是导入pom文件的依赖信心及插件信息，我这里以Springboot 2.0.5为例，其依赖如下;
  
  3.3.在数据建好表之后，建表之前先想好数据的领域模型，这样可以避免后面的添加等麻烦，然后在resources下新建mapper文件夹，
在miaoshaproject文件夹下新建dao和dataobject文件夹，用于可以利用Mybatis的逆向工程生成sql编写的mapper配置文件和生成Dao类的存放位置
以及dataobject的存放，省去跟数据库打交道的代码编写，专注于业务逻辑的开发。resources下新建mybatis-generator.xml文件，编写pojo的生成方式：

  3.4.然后到run目录的->Edit Configuration下点击+按钮新建一个Maven，在Parameter设置文件的位置，以及文件产生的命令
mybatis-generator:generate，Name设置一下，到时候点击run的时候可以切换到App或mybatis-generate的运行。
 
  3.5.使用SpringMvc方式来进行业务开发，首先要明确dataobject是完全与数据库的字段一一映射的，其实现类dao是不能够直接暴露给前端用户，
而是需要另外建一个model模型，这是核心领域模型，然后通过一个VO把需要展现的信息展现给前端并不包括需要隐藏的信息，并通过一个CommonReturnType来定义通用的返回形式来展现给前端，另外通过controller层来定义业务逻辑，通过service层接口和实现来定义controller层
需要调用的方法，来完成controller需要完成的业务，一般使得controller层越简单越好，其余的实现在service层进行编写，达到想要的目的。


