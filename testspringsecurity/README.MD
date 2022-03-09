## 快速上手 基于 spring boot 2.0.5.RELEASE
2.x 版本与之前版本区别是在这个版本，密码必须要进行加密

* 第一步，引包 
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```
* 第二步，开启配置 ,并让配置类继承自 WebSecurityConfigurerAdapter
```java
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    
}
```

* 第三步，配置 UserDetailsService 和密码编码器 

   * 使用内存用户
   ```java
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("9420").password(new BCryptPasswordEncoder().encode("123")).authorities("p1")
        .and()
        .withUser("sanri").password(new BCryptPasswordEncoder().encode("123")).authorities("p2");
    }
   ```
   
   * 使用密码编码器 BCryptPasswordEncoder
   
   ```java
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    ```
* 第四步，配置路径访问权限 

```java
@Override
protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers("/r/r1").hasRole("p1")
            .antMatchers("/r/r2").hasRole("p2")
            .anyRequest().permitAll()
            .and()
            .formLogin()            // 使用 spring 自带的 form 登录表单
    ;
}
```

* 权限配置相关已经完成，再用两个路径来测试权限 
```java
@RestController
public class ResourceController {
    @GetMapping(value = "/r/r1")
    public String r1(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String writeMsg = principal.getUsername() + " 访问 " + request.getRequestURI();
        return writeMsg;
    }

    @GetMapping(value = "/r/r2")
    public String r2(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();

        String writeMsg = principal.getUsername() + " 访问 " + request.getRequestURI();
        return writeMsg;
    }
}
```
上面使用内存中的用户，使用角色来控制权限访问，当 9420 访问 /r/r1 是有权限的，访问 /r/r2 无权限; sanri 访问 /r/r2 是有权限的，但访问 /r/r1 无权限 

## 配置说明  
控制权限一般来说是使用 RABC(role based access control) ，就我们常见的 5 张表的解决方案 User,Role,Resource,User-Role ,Role-Resource ，

一个资源可以是指某个页面，某个方法，或者某个按扭。对资源的控制可以使用角色来控制，具有某个角色的用户才可以访问这个资源; 也可以使用资源来控制，设定用户具有哪些资源的权限，如果有资源的权限就可以访问。

所以我们查出的数据应该是这样子的 `userId[res1,res2,res3...]`，然后我们可以在过滤器那进行拦截所有的请求，然后对请求配置相应的权限，
如可以匿名，需要登录，需要某种资源的权限，需要某种角色的权限，甚至我们可以配置其需要某些角色的任意一种或者需要好几个资源同时拥有才能有权限。
所以对于配置 SpringSecurity 来说，我们需要配置的是，
* 框架应该去哪里去找到我们的用户和权限信息(内存,jdbc,文件...)  UserDetailsService; 在后面的版本中，这个被简化为使用 AuthenticationManagerBuilder 来构建
* 哪些路径分别是什么权限，这些包含前端路径和后端路径  HttpSecurity
* 密码的加密策略是什么 PasswordEncoder

## spring security 原理说明 
首先 spring security 是使用 Filter 来保护应用的，然后主要关心的是创建了哪些过滤器，在哪创建的，过滤器的功能是什么

先看在哪创建的，spring 使用 servlet3 的 SPI 规范创建了一个 ServletContainerInitializer 的实现类 SpringServletContainerInitializer 这个文件在 
spring-web 包的 META-INF/services/ 目录下，然后这个类又拉起了所有 WebApplicationInitializer 的实现类，调用其 onStartup 方法，
在 AbstractSecurityWebApplicationInitializer 这个类中注册了  DelegatingFilterProxy  过滤器，这个过滤器是个代理过滤器，
他会找在 IOC 容器中找 filter-name 名称的 bean 做为真正的过滤器，这个名称就是 springSecurityFilterChain，然后这个 bean 在 @EnableWebSecurity 配置中注册了,
所以最终于找到的过滤器就是 springSecurityFilterChain 

引用 spring 文档 
> Spring Security’s web infrastructure should only be used by delegating to an instance of FilterChainProxy. The security filters should not be used by themselves.

可以知道，上面使用的 springSecurityFilterChain 其实就是  FilterChainProxy ，通过代理去访问真正的过滤器，而不是每次都走完所有的过滤器 

那么具体的过滤器是在哪创建的呢，是在 WebSecurityConfigurerAdapter 的 configure 方法中，我在在 HttpSecurity 使用的那些方法，其实有些就会在后面帮我们创建过滤器 

那么会有哪些过滤器呢 
* SecurityContextPersistenceFilter 该过滤器用于在我们请求到来时，将SecurityContext从Session中取出放入SecuirtyContextHolder中供我们使用。并在请求结束时将SecuirtyContext存进Session中便于下次使用
* DefaultLoginPageGeneratingFilter 过滤器在我们没有自定义配置loginPage时会自动生成，用于生成我们默认的登录页面
* UsernamePasswordAuthenticationFilter 用户名密码认证过滤器
* RememberMeAuthenticationFilter 记住我过滤器
* BasicAuthenticationFilter 基础认证过滤器
* ..... 

主要的过滤器还是认证和授权  
UsernamePasswordAuthenticationFilter 这个过滤器用于拦截表单的请求，默认为 /login 进行用户的认证 ,ExceptionTranslationFilter 该过滤器主要用来捕获处理spring security抛出的异常，异常主要来源于FilterSecurityInterceptor
FilterSecurityInterceptor 主要用于授权判断 

不想抄了，读者自行查看吧 
[https://www.cnblogs.com/wutianqi/p/9186645.html](https://www.cnblogs.com/wutianqi/p/9186645.html)
