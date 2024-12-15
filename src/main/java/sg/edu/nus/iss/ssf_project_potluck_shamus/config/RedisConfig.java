package sg.edu.nus.iss.ssf_project_potluck_shamus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import sg.edu.nus.iss.ssf_project_potluck_shamus.constant.*;;

@Configuration
public class RedisConfig {
        // D15 - slide 17

    //@Value is a Spring annotation used to inject values into variables. It can read values from:
    // application.properties or application.yml: Most common usage.
    // System Environment Variables: If defined.
    // Default Values: If a property is missing, you can specify a default.

    @Value("${spring.data.redis.host}") 
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private Integer redisPort;

    // Default example
    // @Value("${redis.username:default_user}")
    // private String redisUsername;
    // If redis.username isn’t defined in application.properties, the value defaults to "default_user".
    @Value("${spring.data.redis.username}")
    private String redisUsername;

    @Value("${spring.data.redis.password}")
    private String redisPassword;


    // D15 - slide 18
    // Creates a configuration for connecting to a standalone Redis server
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {

        // redisstandalone defines basic connections settings for a single redis server
        RedisStandaloneConfiguration rsc = new RedisStandaloneConfiguration();
        rsc.setHostName(redisHost); 
        rsc.setPort(redisPort);

        // if username is blank or only white spaces, redis connects without authentication
        if(redisUsername.trim().length() > 0) {
            rsc.setUsername(redisUsername);
            rsc.setPassword(redisPassword);
        }

        JedisClientConfiguration jcc = JedisClientConfiguration.builder().build();
        JedisConnectionFactory jcf = new JedisConnectionFactory(rsc, jcc);
        jcf.afterPropertiesSet();

        return jcf;
    }

    // Defines a RedisTemplate bean for interacting with Redis
    @Bean(Constant.template01)
    public RedisTemplate<String, String> redisObjectTemplate01() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }

    // Defines a RedisTemplate bean for interacting with Redis
    @Bean(Constant.template02)
    public RedisTemplate<String, String> redisObjectTemplate02() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer()); // map has key
        template.setHashKeySerializer(new StringRedisSerializer()); // map has hashkey
        template.setHashValueSerializer(new StringRedisSerializer()); // map has hashvalue

        return template;
    }
}
