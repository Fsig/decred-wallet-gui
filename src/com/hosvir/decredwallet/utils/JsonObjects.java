package com.hosvir.decredwallet.utils;

/**
 * @author fsig
 * @version 1.00
 * @since 20/03/17
 */
public class JsonObjects {
    private String name;
    private String value;

    public JsonObjects(String name, String value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
