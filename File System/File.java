/*
 * Name: Joshua Turner
 * Date: 4-18-2020
 * File: FileSystem.java
 * Class: CMSC 412
 * Professor: Alin Suciu
 * Homework 5 File Processing
*/

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class File extends Content{
    
    private String name;
    private Directory parent;
    private String file;
    private byte[] content;
    
    public File(String name, Directory parent, String file) { 
        this.name = name;
        this.parent = parent;
        this.file = file;
        try {
        content = Files.readAllBytes(Paths.get(file));
        }catch(IOException ioe) {
            System.out.println("!!!---Error creating File---!!!");
        }
    }

    public File(String name, Directory parent, byte[] content) {
        this.name = name;
        this.parent = parent;
        this.content = content;
    }
    
    @Override
    public String getName() {
        return name;
    }

    public Directory getParent() {
        return parent;
    }
    
    public byte[] getBytes() {
        return this.content;
    }

    public void setBytes(byte[] content) {
        this.content = content;
    }
    
    
    
    public void writeToFile () {
        try {
        Path path = Paths.get(this.file);
        Files.write(path, this.content);
        }catch(IOException ioe) {
            System.out.println("!!!---Input Output Exception---!!!");
        }
    }
    
    @Override
    public void listAllContent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ArrayList<Content> getContent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
    
    
}
