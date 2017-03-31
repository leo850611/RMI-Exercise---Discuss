//*******************************************************************
//*  Network Programming - Unit 5 Remote Method Invocation          *
//*  Program Name: ArithmeticInterface                              *
//*  The program defines the interface for Java RMI.                *
//*  2014.02.26                                                     *
//*******************************************************************
import java.rmi.Remote;

public interface ArithmeticInterface extends Remote {
	public String register(String user, String password) throws java.rmi.RemoteException;
    public int login(String user, String password) throws java.rmi.RemoteException;
	public int create(String id, String name, String content) throws java.rmi.RemoteException;
	public String subject() throws java.rmi.RemoteException;
	public int reply(int no, String username, String re) throws java.rmi.RemoteException;
	public String discussion(int no) throws java.rmi.RemoteException;
    public int delete(int no) throws java.rmi.RemoteException;
}

