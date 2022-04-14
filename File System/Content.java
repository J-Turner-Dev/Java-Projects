/*
 * Name: Joshua Turner
 * Date: 4-18-2020
 * File: FileSystem.java
 * Class: CMSC 412
 * Professor: Alin Suciu
 * Homework 5 File Processing
*/

import java.util.ArrayList;

abstract public class Content {
       
    abstract public String getName();
    abstract public void listAllContent();
    abstract public ArrayList<Content> getContent();
    abstract public byte[] getBytes();
    abstract public void setBytes(byte[] content);
    
}
