//*******************************************************************
//*  Network Programming - Unit 5 Remote Method Invocation          *
//*  Program Name: ArithmeticRMIImpl                                *
//*  The program implements the services defended in the interface, *
//*    ArithmeticInterface.java, for Java RMI.                      *
//*  2014.02.26                                                      *
//*******************************************************************
import java.rmi.*;
import java.rmi.server.*;

import java.io.*;
import java.util.Scanner;


public class ArithmeticRMIImpl extends UnicastRemoteObject implements ArithmeticInterface
{
	int flag = 0;
    String line;
    Scanner file = null;
    // This implementation must have a public constructor.
	// The constructor throws a RemoteException.
	public ArithmeticRMIImpl() throws java.rmi.RemoteException {
		super(); 	// Use constructor of parent class
	}
		
	// Implementation of the service defended in the interface
	//public long add(long a, long b) throws java.rmi.RemoteException
	public String register(String user, String password) throws java.rmi.RemoteException{	
		if(search_user(user)){
			return ("Err: username already exists.\n");
		}
		else{
            try{
                PrintWriter fileOut = null;
                fileOut = new PrintWriter(new FileOutputStream("db.txt",true));
                fileOut.println(user +' '+ password);
                fileOut.close();
            } catch(FileNotFoundException e) {
                System.out.println("Err: Cannot open the db.");
            }
            return ("Register successful!\n");
        }			
	}
	
	public int login(String user, String password) throws java.rmi.RemoteException{
        flag = 0;
        file = null;
        try{
			file = new Scanner(new FileInputStream("db.txt"));
		} catch(FileNotFoundException e) {
			System.out.println("Err: Cannot open the db.");
		}
        while(file.hasNextLine()){
			line = file.nextLine();
            String[] tokens = line.split(" ");
            
            if(tokens[0].equals(user) && tokens[1].equals(password)){
				flag = 1;
			}			
		}
		file.close();
        
		if(flag==1)
			return 1;
		else
			return 0;
	}
    
    public int create(String id, String name, String content)throws java.rmi.RemoteException{
        if(search_list(name)){
			return 0;
		}
        else{
            try{
                PrintWriter fileOut = null;
                fileOut = new PrintWriter(new FileOutputStream("list.txt",true));
                fileOut.println(id +" $~ "+ name +" $~ "+ content);
                fileOut.close();
            } catch(FileNotFoundException e) {
                System.out.println("Err: Cannot open the list.");
            }
            return 1;
        }
    }
    
    public String subject()throws java.rmi.RemoteException{
        String subjectNane = "->";
        file = null;
        int num = 1;
        try{
			file = new Scanner(new FileInputStream("list.txt"));
		} catch(FileNotFoundException e) {
			System.out.println("Err: Cannot open the list.");
		}
        while(file.hasNextLine()){
			line = file.nextLine();
            String[] tokens = line.split(" \\$~ ");
            subjectNane = (subjectNane + '['+ num +'.'+ tokens[0] +']');
            num++;
		}
		file.close();
        return subjectNane;
    }
    
    
    
    
	private boolean search_user(String user) {
		flag = 0;
        file = null;
        try{
			file = new Scanner(new FileInputStream("db.txt"));
		} catch(FileNotFoundException e) {
			System.out.println("Err: Cannot open the db.");
		}
        while(file.hasNextLine()){
			line = file.nextLine();
            String[] tokens = line.split(" ");
            if(tokens[0].equals(user)){
				flag = 1;
			}			
		}
		file.close();
        
		if(flag==1)
			return true;
		else
			return false;
	}
    
    private boolean search_list(String name) {
		flag = 0;
        file = null;
        try{
			file = new Scanner(new FileInputStream("list.txt"));
		} catch(FileNotFoundException e) {
			System.out.println("Err: Cannot open the list.");
		}
        while(file.hasNextLine()){
			line = file.nextLine();
            String[] tokens = line.split(" $~ ");
            if(tokens[0].equals(name)){
				flag = 1;
			}			
		}
		file.close();
        
		if(flag==1)
			return true;
		else
			return false;
	}

}