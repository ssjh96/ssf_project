package sg.edu.nus.iss.ssf_project_potluck_shamus.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

// import sg.edu.nus.iss.vttp5a_ssf_day16l.model.Person;
import sg.edu.nus.iss.ssf_project_potluck_shamus.constant.Constant;

@Repository
public class ListRepo {

    @Autowired
    @Qualifier(Constant.template01)
    RedisTemplate<String, String> template;

    // Count para
    // Positive number (n): Remove up to n occurrences starting from the head (beginning) of the list.
    // Negative number (-n): Remove up to n occurrences starting from the tail (end) of the list.
    // Zero (0): Remove all occurrences of the value from the list.


    // Adding to List (Single)
    // D15 - slide 30, slide 34
    public void leftPush(String key, String value) {
        template.opsForList().leftPush(key, value);
    }
    
    public void rightPush(String key, String value) {
        template.opsForList().rightPush(key, value);
    }

    // Adding to List (Multiple)
    public void rightPushAll(String key, String... values) {
        template.opsForList().rightPushAll(key, values);
    }
    


    // Removes from List (RPOP)
    // D15 - slide 30
    public void leftPop(String key) {
        template.opsForList().leftPop(key, 1);
    }

    public void rightPop(String key) {
        template.opsForList().rightPop(key, 1);
    }

    // Retrieves and removes the last element of the list.
    // public String rightPop(String key) {
    //     return template.opsForList().rightPop(key);
    // }



    // .remove method 
    // Removes a specific occurence of an element from a Redis List
    // You specify list key, the number of occurences to remove, & value of the lement to remove
    // Redis searches the list and removes matching elements
    
    // Remove an Element from a List - removes the first occurance of a specific value from a list (LREM)
    // Searches and remove that specified one
    // LREM k1 1 e1 = remove from key1, 1 occurance of element1
    public void remove(String key, long count, String value) {
        template.opsForList().remove(key, count, value);
    }

    
    public void removeone(String key, String valueString) {
        template.opsForList().remove(key, 1, valueString); // Removes one occurrence of the value
    }
    


    // Trim a List - Trims the list to only include elements within a specified range. (LTRIM)
    // Keeps only the elements between start and end.
    public void trim(String key, long start, long end) {
        template.opsForList().trim(key, start, end);
    }
    


    // D15 - slide 32
    public String get(String key, Integer index) {
        return template.opsForList().index(key, index).toString();
    }

    // D15 - slide 33
    public Long size(String key) {
        return template.opsForList().size(key);
    }

    // Get the whole list
    public List<String> getList(String key) {
        List<String> list = template.opsForList().range(key, 0, -1);

        return list;
    }

}
