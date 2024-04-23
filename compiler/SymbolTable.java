/**
* Will Store Variables Needed By Compiler In A HashMap
* @author Truong Le
* @version 1.0
* Assignment 5
* CS322 - Compiler Construction
* Spring 2024
**/

package compiler;
import java.util.HashMap;


public class SymbolTable {
    
    private static HashMap<String, Variable> table = new HashMap<>();

    /**
     * Put data into HashMap with a key
     * @param key Key for HashMap
     * @param data Data to pair with the Key
     */
    public void put(String key, Variable data){table.put(key, data);}

     /**
     * Get data from HashMap with a key
     * @param key Key of data to get
     * @return Return the Variable at this certain key
     */
    public Variable get(String key){return table.get(key);}

     /**
     * Remove data with key
     * @param key Key of data to remove
     */
    public void remove(String key){table.remove(key);}

     /**
     * See if the HashMap contains data from a key
     * @param key Key for data to see if the HashMap contains it
     * @return Return true if contains else false
     */
    public boolean containsKey(String key){return table.containsKey(key);}

     /**
     * Get size of the HashMap
     */
    public void size(){table.size();}

     /**
     * See if the HashMap is empty
     */
    public void isEmpty(){table.isEmpty();}

     /**
     * Will print the HashMap
     */
    public void printTable(){
        for (HashMap.Entry<String, Variable> entry : table.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    /**
     * Will print everything about the HashMap
     */
    public void printTableAll(){
        for (HashMap.Entry<String, Variable> entry : table.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + ", stack location : " + entry.getValue().getStackLocation() + ", data : " + entry.getValue().getData() + ", type : " + entry.getValue().getType());
        }
    }

}
