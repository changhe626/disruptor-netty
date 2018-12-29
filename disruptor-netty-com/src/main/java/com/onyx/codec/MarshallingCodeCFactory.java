package com.onyx.codec;

import io.netty.handler.codec.marshalling.DefaultMarshallerProvider;
import io.netty.handler.codec.marshalling.DefaultUnmarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingDecoder;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import io.netty.handler.codec.marshalling.UnmarshallerProvider;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

/**
 * Marshalling工厂
 * JBoss Marshalling是一个Java对象的序列化API包，修正了JDK自带的序列化包的很多问题，但又保持跟java.io.Serializable接口的兼容；
 * 同时增加了一些可调的参数和附加的特性，并且这些参数和特性可通过工厂类进行配置
 */
public final class MarshallingCodeCFactory {

    /**
     * 创建Jboss Marshalling解码器MarshallingDecoder
     * @return MarshallingDecoder
     */
    public static MarshallingDecoder buildMarshallingDecoder() {
		//首先通过Marshalling工具类的getProvidedMarshallerFactory静态方法获取MarshallerFactory实例
		//参数“serial”表示创建的是Java序列化工厂对象，它由jboss-marshalling-serial-1.3.0.CR9.jar提供。
		final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
		//创建了MarshallingConfiguration对象，配置了版本号为5 
		final MarshallingConfiguration configuration = new MarshallingConfiguration();
		configuration.setVersion(5);
		//然后根据MarshallerFactory和MarshallingConfiguration创建UnmarshallerProvider实例
		UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);
		//最后通过构造函数创建Netty的MarshallingDecoder对象
		//它有两个参数，分别是UnmarshallerProvider和单个消息序列化后的最大长度。
		MarshallingDecoder decoder = new MarshallingDecoder(provider, 1024 * 1024 * 1);
		return decoder;
    }

    /**
     * 创建Jboss Marshalling编码器MarshallingEncoder
     * @return MarshallingEncoder
     */
    public static MarshallingEncoder buildMarshallingEncoder() {
		final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
		final MarshallingConfiguration configuration = new MarshallingConfiguration();
		configuration.setVersion(5);
		MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);
		//构建Netty的MarshallingEncoder对象，MarshallingEncoder用于实现序列化接口的POJO对象序列化为二进制数组
		MarshallingEncoder encoder = new MarshallingEncoder(provider);
		return encoder;
    }

}
/**POM
 <dependency>
 <groupId>org.jboss.marshalling</groupId>
 <artifactId>jboss-marshalling</artifactId>
 <version>1.3.0.GA</version>
 </dependency>
 <dependency>
 <groupId>org.jboss.marshalling</groupId>
 <artifactId>jboss-marshalling-serial</artifactId>
 <version>1.3.0.GA</version>
 </dependency>
 */