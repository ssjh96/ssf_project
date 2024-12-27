package sg.edu.nus.iss.ssf_project_potluck_shamus.repository;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.ssf_project_potluck_shamus.constant.Constant;

@Repository
public class MapRepo {

    @Autowired
    @Qualifier(Constant.template01)
    RedisTemplate<String, String> redisTemplate;

    // HSET - Add a key-value pair to Redis hash 
    // hset c0 email fred@gmail.com & hset c0 credit 100
    // Code = template.opsForHash().put(“c0”, “email”, “fred@gmail.com”); & template.opsForHash().put(“c0”, “credit”, 100);
    public void put(String redisKey, String fieldKey, String value)
    {
        // there is .put, .putall, . putifabsent
        redisTemplate.opsForHash().put(redisKey, fieldKey, value);
    }



    // HGET - Get a value from a Redis hash
    // hget c0 email & hget c0 credit
    public Object get(String redisKey, String fieldKey) 
    {
        return redisTemplate.opsForHash().get(redisKey, fieldKey);
    }
    // Returning as object allows String casting since we use StringRedisSerializer 
    // String email = (String) mapRepo.get("c0", "email");  // If you know it's a String
    

    
    // HDEL - Delete a field from a Redis hash
     // hdel c0 email
     // hdel c0 f1 f2 f3 -> if done, returns (interger) 3
    public Boolean delete(String redisKey, String fieldKey)
    {
        Boolean isDeleted = false;
        Long iFound = redisTemplate.opsForHash().delete(redisKey, fieldKey);
        // .delete() returns a Long count of how many fields/hash keys were deleted
        
        // Check if at least one field was deleted
        if (iFound > 0) 
        { 
            isDeleted = true; // If Redis returned > 0, the field was successfully deleted
        }

        return isDeleted; // Returns true if deletion was successful, false otherwise
    }

    public void deleteEvent(String redisKey, String fieldKey)
    {
        redisTemplate.opsForHash().delete(redisKey, fieldKey);
    }



    // HEXISTS - Check if a field exists in a Redis hash 
    // hexists c0 email
    public Boolean hasField(String redisKey, String fieldKey)
    {
        return redisTemplate.opsForHash().hasKey(redisKey, fieldKey);
    }



    // HGETALL - Get the entire hash as a Map. Returns all key-value pairs in the hash.
    // HGETALL mapKey
    public Map<Object, Object> getAll(String redisKey)
    {
        return redisTemplate.opsForHash().entries(redisKey);
    }

    // HKEYS - gets all the field keys using redis key
    // hkeys hello -> f1, f2, f3
    public Set<Object> getKeys(String redisKey) // distinct keys thats why use set
    {
        return redisTemplate.opsForHash().keys(redisKey);
    }


    // HVALS - Get all values in a Redis hash using redis key
    // hvals hello -> v1, v2, v3
    // List<Object> values = template.opsForHash().values(“c01”)l
    public List<Object> getValues(String redisKey) // values can be repeated so can use list
    {
        return redisTemplate.opsForHash().values(redisKey);
    }



    // HLEN - Get the size of a Redis hash using redis key
    // hlen hello -> 3
    // long mapSize = template.opsForHash().size(“c01”);
    public Long size (String redisKey)
    {
        return redisTemplate.opsForHash().size(redisKey);
    }


    // Increment a numeric value in a Redis hash (HINCRBY)
    // hincrby c01 count 1
    public void incrementLong(String mapKey, String fieldKey, long value) {
        redisTemplate.opsForHash().increment(mapKey, fieldKey, value);
    }


    // Increment a float value in a Redis hash (HINCRBY)
    // hincrby c01 count 1
    public void incrementDouble(String mapKey, String fieldKey, double value) {
        redisTemplate.opsForHash().increment(mapKey, fieldKey, value);
    }

    
    // EXPIRE - set redis key's TTL, check with "ttl <redisKey>"
    // expire hello 100
    public void expire(String redisKey, Integer expireValue)
    {
        Duration expireDuration = Duration.ofSeconds(expireValue);

        redisTemplate.expire(redisKey, expireDuration);
    }


    // TTL - check time to live
    // ttl hello
    public Long ttlCheck (String redisKey)
    {
        Long ttl = redisTemplate.getExpire(redisKey);

        return ttl;
    }


    // PERSIST - cancel expire redis key
    // persist hello
    public Boolean persist(String redisKey)
    {
        redisTemplate.persist(redisKey);

        Long ttl = redisTemplate.getExpire(redisKey);

        if (ttl == -1)
        {
            return true; // Key is now persistent
        }
        
        if (ttl == -2)
        {
            System.out.println("Redis Key does not exist."); // Key doesn't exist
        }

        return false; // Either key doesn't exist or persistence failed
    }
}
