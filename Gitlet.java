import java.io.*;


public class Gitlet {
	public static void main(String[] args) {
        GitletSystem gs;
		if (args == null || args.length==0) {
			System.out.println("enter a command please");
		} else {
			String[] commandlist = args;
			String command = args[0];
			String[] tokens = new String[commandlist.length-1];
			System.arraycopy(commandlist,1,tokens,0, commandlist.length-1);
            switch (command) {
                case "init": 
                    File f = new File(".gitlet/");
                    if(!f.exists()) {
                        f.mkdir();
                        
                    	gs = new GitletSystem();
                    	gs.serialize();
                    } else {
                    	System.out.println("A gitlet version control system already exists in the current directory.");
                    }
                    break;
                case "add":
                    gs = GitletSystem.deserialize();
                    gs.add(tokens[0]);
                    gs.serialize();
                    break;
                case "commit":
                    gs = GitletSystem.deserialize();
                    if (args.length < 2) {
                    	System.out.println("missing commit message");
                    } else {
                    	gs.commit(tokens[0]);
                    }
                    gs.serialize();
                    break;
                case "log":
                    gs = GitletSystem.deserialize();
                    gs.log();
                    break;


            }
        }
                
                    
	}





}