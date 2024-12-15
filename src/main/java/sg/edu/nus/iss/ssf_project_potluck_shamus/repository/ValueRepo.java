package sg.edu.nus.iss.ssf_project_potluck_shamus.repository;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.ssf_project_potluck_shamus.constant.Constant;

@Repository
public class ValueRepo {
    
    // D15 - slide 20

    @Autowired
    @Qualifier(Constant.template01)
    RedisTemplate<String, String> template;

    // Create or update a value in Redis (SET)
    // set key value
    // D15 - slide 24 - create/update a value 
    public void createValue(String key, String value) {
        // Creates or updates the value for the specified key
        template.opsForValue().set(key, value);

        // Alternate operations:
        // - setIfPresent: Sets the value only if the key exists
        // - setIfAbsent: Sets the value only if the key does not exist
    }

    // Retrieve a value from Redis (GET)
    // get key
    // D15 - slide 25 -retrieve a value
    public String getValue(String key) {
        // Retrieves the value associated with the specified key
        return template.opsForValue().get(key);
    }


    // Delete a key-value pair from Redis (DEL)
    // del key
    // D15 - slide 27 - delete
    public Boolean deleteValue(String key) {
        // Deletes the key-value pair and returns true if the key was deleted
        return template.delete(key);
    }

    // Increment an integer value stored in Redis by 1 (INCR)
    // incr key
    // D15 - slide 26 - only works for key with integer value
    public void incrementValue(String key) {
        // Increments the value of the specified key by 1
        template.opsForValue().increment(key);
    }

    // Decrement an integer value stored in Redis by 1 (DECR)
    // decr key
    public void decrementValue(String key) {
        // Decrements the value of the specified key by 1
        template.opsForValue().decrement(key);
    }

    // Increment an integer value by a specific amount (INCRBY)
    // incrby key increment
    public void incrementByValue(String key, Integer value) {
        // Increments the value of the specified key by the given amount
        template.opsForValue().increment(key, value);
    }

    // Decrement an integer value by a specific amount (DECRBY)
    // decrby key decrement
    public void decrementByValue(String key, Integer value) {
        // Decrements the value of the specified key by the given amount
        template.opsForValue().decrement(key, value);
    }

    // Check if a key exists in Redis (EXISTS)
    // exists key
    // D15 - slide 28
    public Boolean checkExists(String key) {
        // Returns true if the specified key exists, false otherwise
        return template.hasKey(key);
    }


    // Append a value to an existing value (APPEND)
    // append key value
    // Append to a value - Appends a value to an existing value stored at a key. (APPEND)
    public void append(String key, String value) {
        // Appends the specified value to the existing value of the key
        template.opsForValue().append(key, value);
    }

    // Get a substring of a value stored in Redis (GETRANGE)
    // getrange key start end
    // Get a substring of a value - Retrieves a substring of the string stored at a key. (GETRANGE)
    public String getSubstring(String key, long start, long end) {
        // Retrieves a substring of the string stored at the specified key
        return template.opsForValue().get(key, start, end);
    }


    // Set a value only if the key does not exist (SETNX)
    // setnx key value
    // Set a Value Only If It Doesn’t Exist - Ensures that the value is only set if the key does not already exist.  (SETNX)
    public Boolean setIfAbsent(String key, String value) {
        // Sets the value only if the key does not already exist
        return template.opsForValue().setIfAbsent(key, value);
    }


    // Expire a Key - Sets a time-to-live (TTL) for a key. (EXPIRE)
    public Boolean setExpiration(String key, long timeout, TimeUnit unit) {
        return template.expire(key, timeout, unit);
    }

    // Set a value with a time-to-live (SETEX)
    // setex key seconds value
    public void setValueWithExpiration(String key, String value, long timeout, TimeUnit unit) {
        // Sets a value and assigns a TTL in one step
        template.opsForValue().set(key, value, timeout, unit);
    }

    // Get and set a new value (GETSET)
    // getset key value
    public String getAndSet(String key, String newValue) {
        // Sets the new value and returns the old value
        return template.opsForValue().getAndSet(key, newValue);
    }

    // Set a value at a specific offset (SETRANGE)
    // setrange key offset value
    public void setValueAtOffset(String key, long offset, String value) {
        // Overwrites part of the string stored at the key, starting at the specified offset
        template.opsForValue().set(key, value, offset);
    }

    // Delete multiple keys (DEL)
    // del key1 key2 key3
    public Long deleteValues(String... keys) {
        // Deletes the specified keys and returns the count of keys deleted
        return template.delete(List.of(keys));
    }

    // Rename a key (RENAME)
    // rename oldKey newKey
    public void renameKey(String oldKey, String newKey) {
        // Renames a key to a new name
        template.rename(oldKey, newKey);
    }

    // Get all keys matching a pattern (KEYS)
    // keys pattern
    public Set<String> getKeysByPattern(String pattern) {
        // Retrieves all keys matching the given pattern
        return template.keys(pattern);
    }

    // Patterns are like wildcard searches:

    // *: Matches zero or more characters.
    // ?: Matches exactly one character.
    // [abc]: Matches one character from the given set (a, b, or c).
    // [a-z]: Matches any one character in the given range (e.g., a to z).
    // [^abc]: Matches any character not in the set a, b, or c.

    // user:* - Matches keys starting with user: (e.g., user:1, user:profile).
    // session:? - Matches keys like session:1 but not session:123.
    // data[ab]* - Matches keys like dataa or datab, but not datac.
                    // start w data, followed by a or b, followed by zero or more addn characters due to * wildcard
    // data[123] - Matches keys data1, data2, or data3.
    // data[1-5] - Matches keys data1, data2, data3, data4, or data5.
    // log:20* - Matches all keys that start with log:20 (e.g., log:2023, log:2024:01).
    // session:[^a-z]* - Matches keys starting with session: followed by characters not lowercase letters.
    // report:*:* - Matches keys that have at least two colons, e.g., report:2024:12.









    
    
}
