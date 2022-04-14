/*
 * Name: Joshua Turner
 * Date: 4-18-2020
 * File: FileSystem.java
 * Class: CMSC 412
 * Professor: Alin Suciu
 * Homework 5 File Processing
*/

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class FileSystem {


    public static void main(String[] args) {
        
        int programRun = 1;
        String currentDirectory = null;
        int currentIndex = -1;
        String userSelection;
        ArrayList<Directory> directoryList = new ArrayList();
  
        Directory rootDirectory = new Directory("C:", null);
        directoryList.add(rootDirectory);
        Directory folder1 =  new Directory("Folder1", rootDirectory);
        folder1.getParent().getContent().add(folder1);
        directoryList.add(folder1);
        Directory folder2 = new Directory("Folder2", folder1);
        folder2.getParent().getContent().add(folder2);
        directoryList.add(folder2);
        File file1 = new File("File1", folder2, "file.txt");
        file1.getParent().getContent().add(file1);
        File homework5 = new File("homework5", folder2, "homework5.pdf");
        homework5.getParent().getContent().add(homework5);
        
     
        
        
        while(programRun > 0) {
            
            System.out.println("////////////////////----MENU----////////////////////");
            System.out.println("0 - Exit");
            System.out.println("1 - Select Directory");
            System.out.println("2 - List Directory Content(First Level)");
            System.out.println("3 - List Directory Content(All Levels)");
            System.out.println("4 - Delete File");
            System.out.println("5 - Display File(Hexadecimal View)");
            System.out.println("6 - Encrypt File(XOR with Password)");
            System.out.println("7 - Decrypt FIle(XOR with Password)");
            System.out.println("////////////////////////////////////////////////////");
            System.out.print("\nCurrent Selected Directory: ");
            if(currentDirectory == null) {
                System.out.println("No Directory Selected");
            }
            else {
                System.out.println(currentDirectory);
            }
            System.out.println("\nSelect an Option:");
            
            Scanner userInput = new Scanner(System.in);
            userSelection = userInput.next();
            
            if("0".equals(userSelection)) {
                System.out.print("\n!!!---Exiting Program---!!!\n");
                programRun--;
            }
            
            else if("1".equals(userSelection)) {
                
                System.out.println("---Enter a Directory Path---");
                Scanner dirPath = new Scanner(System.in);
                String directoryPath = dirPath.next();
                String selectedName = null;
                for(int i = 0; i<directoryList.size(); i++) {
                    if(directoryList.get(i).getName().compareTo(directoryPath) == 0) {
                        selectedName = directoryList.get(i).getName();
                        currentIndex = i;
                    }
                }
                if(selectedName == null) {
                    System.out.println("!!!---File not Found - Try again---!!!");
                }
                else {
                    currentDirectory = selectedName;
                    System.out.println("The Current Selected Directory is " + currentDirectory);
                }
            }
            
            else if("2".equals(userSelection)) {
                
                if(currentDirectory == null) {
                    System.out.println("No Current Directory");
                }
                else {
                     directoryList.get(currentIndex).listDirectContent();
                }   
            }
            
            else if("3".equals(userSelection)) {
                
                if(currentDirectory == null) {
                    System.out.println("No Current Directory");
                }
                else {
                    directoryList.get(currentIndex).listAllContent();
                    }   
                }
                
            else if("4".equals(userSelection)) {
                
                if(currentDirectory == null) {
                    System.out.println("No Current Directory");
                }
                else {
                    int match = 0;
                    
                    System.out.println("---Select a File to Delete---");
                    Scanner selFile = new Scanner(System.in);
                    String selectedFile = selFile.next();
                    
                    for(int i = 0; i<directoryList.get(currentIndex).getContent().size(); i++) {
                        if(directoryList.get(currentIndex).getContent().get(i).getName().equals(selectedFile)) {
                            directoryList.get(currentIndex).getContent().remove(i);
                            match = 1;
                        }
                    }
                    if(match == 1) {
                        System.out.println("---File Deleted---");
                    }
                    else {
                        System.out.println("!!!---File not Found---!!!");
                    }
                }
                 
            }
            
            else if("5".equals(userSelection)) {
                
                if(currentDirectory == null) {
                    System.out.println("No Current Directory");
                }
                else {
                    int match = 0;
                    
                    System.out.println("---Select a File to Read Hexadecimal View---");
                    Scanner selFile = new Scanner(System.in);
                    String selectedFile = selFile.next();
                    
                    for(int i = 0; i<directoryList.get(currentIndex).getContent().size(); i++) {
                        if(directoryList.get(currentIndex).getContent().get(i).getName().equals(selectedFile)) {
                            
                            StringBuilder strBuilder = new StringBuilder();
                            for(byte val : directoryList.get(currentIndex).getContent().get(i).getBytes()) {
                            strBuilder.append(String.format("%02x", val&0xff) + " ");
                            }
                            System.out.print(strBuilder + "\n");
                            
                            match = 1;
                        }
                    }
                    if(match == 1) {
                        System.out.println("---File Read---");
                    }
                    else {
                        System.out.println("!!!---File not Found---!!!");
                    }
                }
                
            }
            else if("6".equals(userSelection)) {
                
                if(currentDirectory == null) {
                    System.out.println("No Current Directory");
                }
                else {
                    int match = 0;
                    
                    System.out.println("---Select a File to Encrypt---");
                    Scanner selFile = new Scanner(System.in);
                    String selectedFile = selFile.next();
                    
                    for(int i = 0; i<directoryList.get(currentIndex).getContent().size(); i++) {
                        if(directoryList.get(currentIndex).getContent().get(i).getName().equals(selectedFile)) {
                            
                            System.out.println("---Enter a Password---");
                            Scanner passWord = new Scanner(System.in);
                            String xorString = passWord.next();
                            char[] xorArray = xorString.toCharArray();
                            byte[] passwordArray = new byte[xorArray.length];
                            for(int k =0; k<xorArray.length; k++) {
                                passwordArray[k] = (byte) xorArray[k];
                            }
                            byte xorPassword = passwordArray[0];
                            
                            byte[] tempBytes = new byte[directoryList.get(currentIndex).getContent().get(i).getBytes().length];
                            for(int j =0; j<directoryList.get(currentIndex).getContent().get(i).getBytes().length; j++) {
                                
                                tempBytes[j] = (byte) (directoryList.get(currentIndex).getContent().get(i).getBytes()[j] ^ xorPassword);
                            }
                            
                            System.out.println("---Enter new File Name---");
                            Scanner newName = new Scanner(System.in);
                            String fileName = newName.next();
                            
                            File[] tempFile = new File[1];
                            tempFile[0] = new File(fileName, directoryList.get(currentIndex), tempBytes);
                            tempFile[0].getParent().getContent().add(tempFile[0]);
                            
                            match = 1;
                        }
                    }
                    if(match == 1) {
                        System.out.println("---File Encrypted---");
                    }
                    else {
                        System.out.println("!!!---File not Found---!!!");
                    }
                }
                
            }
            
            else if("7".equals(userSelection)) {
                
                if(currentDirectory == null) {
                    System.out.println("No Current Directory");
                }
                else {
                    int match = 0;
                    
                    System.out.println("---Select a File to Decrypt---");
                    Scanner selFile = new Scanner(System.in);
                    String selectedFile = selFile.next();
                    
                    for(int i = 0; i<directoryList.get(currentIndex).getContent().size(); i++) {
                        if(directoryList.get(currentIndex).getContent().get(i).getName().equals(selectedFile)) {
                            
                            System.out.println("---Enter a Password---");
                            Scanner passWord = new Scanner(System.in);
                            String xorString = passWord.next();
                            char[] xorArray = xorString.toCharArray();
                            byte[] passwordArray = new byte[xorArray.length];
                            for(int k =0; k<xorArray.length; k++) {
                                passwordArray[k] = (byte) xorArray[k];
                            }
                            byte xorPassword = passwordArray[0];
                            
                            byte[] tempBytes = new byte[directoryList.get(currentIndex).getContent().get(i).getBytes().length];
                            for(int j =0; j<directoryList.get(currentIndex).getContent().get(i).getBytes().length; j++) {
                                
                                tempBytes[j] = (byte) (directoryList.get(currentIndex).getContent().get(i).getBytes()[j] ^ xorPassword);
                            }
                            
                            System.out.println("---Enter new File Name---");
                            Scanner newName = new Scanner(System.in);
                            String fileName = newName.next();
                            
                            File[] tempFile = new File[1];
                            tempFile[0] = new File(fileName, directoryList.get(currentIndex), tempBytes);
                            tempFile[0].getParent().getContent().add(tempFile[0]);
                            
                            match = 1;
                        }
                    }
                    if(match == 1) {
                        System.out.println("---File Decrypted---");
                    }
                    else {
                        System.out.println("!!!---File not Found---!!!");
                    }
                }
                
            }
            
            else {
                System.out.println("\n!!!---INVALID OPTION---!!! \nSelect a Valid Option.\n");
            }
        }
        
    }
    
}
