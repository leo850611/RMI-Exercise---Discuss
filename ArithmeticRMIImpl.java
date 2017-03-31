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
import java.util.*;
import java.util.concurrent.locks.*;

public class ArithmeticRMIImpl extends UnicastRemoteObject implements ArithmeticInterface
{
	int flag = 0;
    String line;
    Scanner file = null;
    Date d = new Date(); 
    ReadWriteLock lock = new ReentrantReadWriteLock();
    // This implementation must have a public constructor.
	// The constructor throws a RemoteException.      
	
    public ArithmeticRMIImpl() throws java.rmi.RemoteException {
		super(); 	// Use constructor of parent class
	}
		
	// Implementation of the service defended in the interface
	public String register(String user, String password) throws java.rmi.RemoteException{	
		if(search_user(user)){
			return ("Err: username already exists.\n");
		}
		else{
            lock.writeLock().lock();
            try{
                PrintWriter fileOut = null;
                fileOut = new PrintWriter(new FileOutputStream("db.txt",true));
                fileOut.println(user +' '+ password);
                fileOut.close();
            } catch(FileNotFoundException e) {
                System.out.println("Err: Cannot open the db.");
            } finally {
                lock.writeLock().unlock();
            }
            return ("Register successful!\n");
        }			
	}
	
	public int login(String user, String password) throws java.rmi.RemoteException{
        flag = 0;
        file = null;
        lock.writeLock().lock();
        try{
			file = new Scanner(new FileInputStream("db.txt"));
            while(file.hasNextLine()){
                line = file.nextLine();
                String[] tokens = line.split(" ");
                
                if(tokens[0].equals(user) && tokens[1].equals(password)){
                    flag = 1;
                }			
            }
            file.close();
		} catch(FileNotFoundException e) {
			System.out.println("Err: Cannot open the db.");
		} finally {
            lock.writeLock().unlock();
        }
        
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
            lock.writeLock().lock();
            try{
                PrintWriter fileOut = null;
                fileOut = new PrintWriter(new FileOutputStream("list.txt",true));
                d = new Date();
                fileOut.println(id +" $~ "+ d.toString() +" $~ "+ name +" $~ "+ content);
                fileOut.close();
            } catch(FileNotFoundException e) {
                System.out.println("Err: Cannot open the list.");
            } finally {
                lock.writeLock().unlock();
            }
            return 1;
        }
    }
    
    public String subject()throws java.rmi.RemoteException{
        String subjectNane = "->";
        file = null;
        int num = 1;
        lock.writeLock().lock();
        try{
			file = new Scanner(new FileInputStream("list.txt"));
		} catch(FileNotFoundException e) {
			System.out.println("Err: Cannot open the list.");
		} finally {
            lock.writeLock().unlock();
        }
        while(file.hasNextLine()){
			line = file.nextLine();
            String[] tokens = line.split(" \\$~ ");
            subjectNane = (subjectNane + '['+ num +'.'+ tokens[2] +']');
            num++;
		}
		file.close();
        return subjectNane;
    }
    
    public int reply(int no, String username, String re)throws java.rmi.RemoteException{
		int i = 1;
		String name = null;
		flag = 0;
        file = null;
        lock.writeLock().lock();
        try{
			file = new Scanner(new FileInputStream("list.txt"));
            while(file.hasNextLine()){
                line = file.nextLine();
                String[] tokens = line.split(" \\$~ ");
                if(no== i){
                    name = tokens[2];
                    flag = 1;
                }
                i++;
            }
            file.close();            
		} catch(FileNotFoundException e) {
			System.out.println("Err: Cannot open the db.");
		} finally {
            lock.writeLock().unlock();
        }
		
		int cleck = 0;
		if(flag ==1){
			lock.writeLock().lock();
            try{
				File f = null;
				f = new File(name+".txt");
				if(f.exists() == false){
					f.createNewFile();
				}
				
				try{
					PrintWriter fileOut = null;
					fileOut = new PrintWriter(new FileOutputStream(name+".txt",true));
					d = new Date();
					fileOut.println(username +" $~ "+ d.toString() +" $~ "+ re);
					fileOut.close();
					cleck = 1;
				} catch(FileNotFoundException e) {
					System.out.println("Err: Cannot open the list.");
				}
			} catch(Exception e){
				e.printStackTrace();
			} finally {
                lock.writeLock().unlock();
            }
		}

		if(cleck ==0)
			return 0;
		else
			return 1;		
	}
	
	public String discussion(int no) throws java.rmi.RemoteException{
        String send = "";
        String path = null;
		int i = 1;
		flag = 0;
        file = null;
        lock.writeLock().lock();
        try{
			file = new Scanner(new FileInputStream("list.txt"));
            while(file.hasNextLine()){
                line = file.nextLine();
                String[] tokens = line.split(" \\$~ ");
                if(no == i){
                    send = send + "PO: " + tokens[0] +'\n';
                    send = send + "Time: " + tokens[1] +'\n';
                    send = send + "Subject: " + tokens[2] +'\n';
                    send = send + "=================================================================\n";
                    path = tokens[2];
                    send = send + "Contents: " + tokens[3] +'\n';
                    flag = 1;
                }
                i++;
            }
            file.close();
            
		} catch(FileNotFoundException e) {
			System.out.println("Err: Cannot open the db.");
		} finally {
            lock.writeLock().unlock();
        }
        
		lock.writeLock().lock();
        try{
            File f = null;
            f = new File(path+".txt");
            if(path!=null && f.exists()){
                try{
                    file = new Scanner(new FileInputStream(path+".txt"));
                    
                    while(file.hasNextLine()){
                        line = file.nextLine();
                        String[] tokens = line.split(" \\$~ ");
                        send = send + "-----------------------------------------------------\n";
                        send = send + "Reply: " + tokens[0] +'\n';
                        send = send + "Time: " + tokens[1] +'\n';
                        send = send + "Contents: " + tokens[2] +'\n';
                    }
                    send = send +'\n';
                    file.close();
                    
                } catch(FileNotFoundException e) {
                    System.out.println("Err: Cannot open the reply.");
                } 
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            lock.writeLock().unlock();
        } 
        
		if(flag == 1)
            return send;
        else
            return "No discussion.";
	}
	
    @SuppressWarnings("unchecked")
	public int delete(int line)throws java.rmi.RemoteException{
		flag = 0;
        lock.writeLock().lock();
		try{
			int num = 0;
			BufferedReader br = new BufferedReader(new FileReader("list.txt"));
			String str = null;
			@SuppressWarnings("rawtypes")
            List list = new ArrayList();
			while( (str=br.readLine()) != null ){
				++num;
				if( num == line ){
					flag = 1;
					continue;
				}
					
				list.add(str);
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter("list.txt"));
			for( int i=0;i<list.size();i++ ){
				bw.write(list.get(i).toString());
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch(IOException e) {
			System.out.println("Err: Cannot open the list.");
		} finally {
            lock.writeLock().unlock();
        }
		
		if(flag ==1){
			return 1;
		}
		else{
			return 0;
		}
	}
	
	
	
	private boolean search_user(String user) {
		flag = 0;
        file = null;
        lock.writeLock().lock();
        try{
			file = new Scanner(new FileInputStream("db.txt"));
            while(file.hasNextLine()){
                line = file.nextLine();
                String[] tokens = line.split(" ");
                if(tokens[0].equals(user)){
                    flag = 1;
                }			
            }
            file.close();
		} catch(FileNotFoundException e) {
			System.out.println("Err: Cannot open the db.");
		} finally {
            lock.writeLock().unlock();
        }
        
		if(flag==1)
			return true;
		else
			return false;
	}
    
    private boolean search_list(String name) {
		flag = 0;
        file = null;
        lock.writeLock().lock();
        try{
			file = new Scanner(new FileInputStream("list.txt"));
            while(file.hasNextLine()){
                line = file.nextLine();
                String[] tokens = line.split(" $~ ");
                if(tokens[0].equals(name)){
                    flag = 1;
                }			
            }
            file.close();
		} catch(FileNotFoundException e) {
			System.out.println("Err: Cannot open the list.");
		} finally {
            lock.writeLock().unlock();
        }
        
		if(flag==1)
			return true;
		else
			return false;
	}

}