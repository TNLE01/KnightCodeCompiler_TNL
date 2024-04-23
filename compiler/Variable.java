/**
* Variable That The HashMap Will Store
* @author Truong Le
* @version 1.0
* Assignment 5
* CS322 - Compiler Construction
* Spring 2024
**/

package compiler;

public class Variable<T> {
    
    private T data;
    private int stackLocation;
    private String type;


    /**
     * Constructor for the class
     */
    public Variable() {
        this.data = null;
        this.stackLocation = 0;
        this.type = "STRING";
    }

    /**
     * Constructor for the class
     * @param stackLocation The location on the stack
     */
    public Variable(int stackLocation) {
        this.data = null;
        this.stackLocation = stackLocation;
        this.type = "STRING";
    }

    /**
     * Constructor for the class
     * @param stackLocation The location on the stack
     * @param type Type of the data
     */
    public Variable(int stackLocation, String type) {
        this.data = null;
        this.stackLocation = stackLocation;
        this.type = type;
    }

    /**
     * Constructor for the class
     * @param stackLocation The location on the stack
     * @param type Type of the data
     * @param data The data this Varible will have
     */
    public Variable(int stackLocation, String type, T data) {
        this.data = data;
        this.stackLocation = stackLocation;
        this.type = type;
    }

    /**
     * Setter for the Variable
     */
    public void setData(T data) {this.data = data;}

    /**
     * Getter for the Variable
     */
    public T getData() {return data;};

    /**
     * Getter for the stack location
     */
    public int getStackLocation() {return stackLocation;}
    
    /**
     * Setter for the data type
     */
    public void setType(String type) {this.type = type;}

    /**
     * Getter for the data type
     */
    public String getType() {return type;}

}
