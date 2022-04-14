/*
 * Name: Joshua Turner
 * Date: 4-18-2020
 * File: FileSystem.java
 * Class: CMSC 412
 * Professor: Alin Suciu
 * Homework 5 File Processing
*/

import java.util.ArrayList;

public class Directory extends Content {
    
    private String name;
    private Directory parent;
    
    private ArrayList<Content> content = new ArrayList();
    
    public Directory(String name, Directory parent) { 
        this.name = name;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return name;
    }

    public Directory getParent() {
        return parent;
    }
    
    public void listDirectContent() {
        
        for(int i =0; i<content.size(); i++) {
            if(content.get(i) instanceof Directory) {
                System.out.println(content.get(i).getName() + "-[Directory]");
            }
            if(content.get(i) instanceof File) {
                System.out.println(content.get(i).getName() + "-[File]");
            }
        }
    }
    
    public void listAllContent() {
        for(int i =0; i<content.size(); i++) {
            if(content.get(i) instanceof Directory) {
                System.out.println(content.get(i).getName() + "-[Directory]");
                content.get(i).listAllContent();
            }
            if(content.get(i) instanceof File) {
                System.out.println(content.get(i).getName() + "-[File]");
            }
        }
    }

    public ArrayList<Content> getContent() {
        return content;
    }

    @Override
    public byte[] getBytes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setBytes(byte[] content) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
       
}
