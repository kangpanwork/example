初次接触这个模式时觉得特别多余,但你用多了 spring 后,你才会觉得这个模式存在的必要,

spring 已经维护了一个 bean 工厂,但这意味着我没有办法扩展自己来决定对象的生成过程,我可能有自己的一些操作,
如 kafka 消费类的容器工厂,搜索 factory 就可以看到一些需要集成进 spring 的一些工具都差不多是使用 factory 来实现的,

spring 有一个 FactoryBean 来做这个事,面试经常会拿这个和 BeanFactory 做区分,说的就是这两种模式