package org.example;

import java.util.*;

public class FlattenMap {

    public static Map<String, Object> flatten(Map<String, Object> map) {
        Map<String, Object> result = new LinkedHashMap<>();
        flattenHelper(map, "", result);
        return result;
    }

    @SuppressWarnings("unchecked")
    public static void flattenHelper(Object obj, String prefix, Map<String, Object> result)
    {
        if(obj== null) return;

        if(obj instanceof Map)
        {
            Map<String, Object> map = (Map<String, Object>) obj;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = prefix.isEmpty()
                        ? entry.getKey()
                        : prefix + "." + entry.getKey();

                flattenHelper(entry.getValue(), key, result);
            }


        }
        else if (obj instanceof List) {
            List<Object> list = (List<Object>) obj;
            for (int i = 0; i < list.size(); i++) {
                // Append bracket notation for array index: key[0], key[1]
                String key = prefix + "[" + i + "]";

                flattenHelper(list.get(i), key, result);
            }
        } else {
            // Leaf node: primitive, String, Boolean, Number, etc.
            result.put(prefix, obj);
        }
    }


    public static void main(String[] args)
    {

        Map<String, Object> user1 = new HashMap<>();
        user1.put("name", "Alice");

        Map<String, Object> user2 = new HashMap<>();
        user2.put("name", "Bob");

        List<Object> usersList = Arrays.asList(user1, user2);

        Map<String, Object> input = new LinkedHashMap<>();
        input.put("users", usersList);
        input.put("status", "active");

        // Flatten and print output
        Map<String, Object> flattenedMap = flatten(input);
        flattenedMap.forEach((k, v) -> System.out.println(k + " = " + v));



    }
}
