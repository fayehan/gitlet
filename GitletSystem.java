import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.nio.file.Files;


public class GitletSystem implements java.io.Serializable{
	public HashMap<Integer, CommitNode> commitNodes;
	public HashSet<File> trackedFiles;
	public HashSet<File> removedFiles;
	public int head;
	public int currentid;


	public GitletSystem() {
		currentid = 0;
		head = 0;
		trackedFiles = new HashSet<File>();
		removedFiles = new HashSet<File>();


		commitNodes = new HashMap<Integer,CommitNode>();

		File a = new File(".gitlet/0/");
		a.mkdirs();

		CommitNode first = new CommitNode(trackedFiles, removedFiles, -1, currentid, "initial commit");
		commitNodes.put(currentid,first);

	}

	public void add(String filepath) {
		File addfile = new File(filepath);
		if (!addfile.exists()) {
			System.out.println("File does not exists");
			return;
		}
		// if (changed(filepath,head)){
		// 	 System.out.println("File has not been modified since the last commit");
		// 	 return;
		//}
		trackedFiles.add(addfile);
	}

	public void commit(String message) {
		if (trackedFiles.size() == 0 && removedFiles.size() == 0) {
            System.out.println("No changes added to the commit.");
            return;
        }
        currentid+=1;
		head = currentid;
		File a = new File(".gitlet/" + currentid+"/");
		a.mkdirs();
		CommitNode cn = new CommitNode(trackedFiles, removedFiles, currentid-1, currentid, message);
		cn.serialize();
		createSnapshot(cn.files(), currentid);
		commitNodes.put(currentid, cn);
		trackedFiles.clear();
		removedFiles.clear();
	}


	public void remove(String filepath) {
		File removefile = new File(filepath);
		if (!trackedFiles.contains(removefile) && !commitNodes.get(head).files().contains(removefile)){
			System.out.println("No reason to remove the file.");
			return;
		}
		removedFiles.add(removefile);
	}

	public void log() {
		CommitNode current= CommitNode.deserialize(currentid);
		while (current.previousid() != -1){
			System.out.println("====");
			System.out.println("Commit "+ current.id() + ".");
			System.out.println(current.date());
			System.out.println(current.message());
			System.out.println("");
			current = CommitNode.deserialize(current.previousid());
		}
		CommitNode zero = CommitNode.deserialize(0);
		System.out.println("====");
		System.out.println("Commit "+ zero.id() + ".");
		System.out.println(zero.date());
		System.out.println(zero.message());
		System.out.println("");


	}



	public void serialize() {
	 	try{
		 	FileOutputStream fout = new FileOutputStream(".gitlet/sys.ser");
		 	ObjectOutputStream out = new ObjectOutputStream (fout);
		 	out.writeObject(this);
		 	out.flush();
		 	out.close();
		 } catch (IOException e) {
		 	e.printStackTrace();
		 }
	}

	public static GitletSystem deserialize() {
		GitletSystem r;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(".gitlet/sys.ser"));
			r = (GitletSystem)in.readObject();
			in.close();
		} catch (IOException i) {
			System.out.println("Commit object not found");
            i.printStackTrace();
            return null;
		} catch (ClassNotFoundException e) {
            System.out.println("Commit class not found");
            e.printStackTrace();
            return null;
        }
		return r;
	}


	// Copies selected files from the staging area into the folder for the commit
    public static void createSnapshot(HashSet<File> files, int commit) {
        for (File filename : files) {
            copyIntoCommit(filename, commit);
        }
    }

    public static void copyIntoCommit(File filename, int commitId) {
        File destination = new File(".gitlet/" + commitId + "/" + filename);
        try {
            Files.copy(filename.toPath(), destination.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


  
}










