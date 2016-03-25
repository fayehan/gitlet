import java.util.ArrayList;
import java.util.HashSet;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CommitNode implements java.io.Serializable{
	public ArrayList<CommitNode> next;
	public HashSet<File> files;
	public int id;
	public int previous;
	public String message;
	public String date;

	public CommitNode(HashSet<File> newfiles, HashSet<File> removefiles, int previous, int id, String message) {
		this.next = new ArrayList<CommitNode>();
		this.id = id;
		this.previous = previous;
		this.message = message;
		this.files= newfiles;
		if(id != 0) {
			CommitNode previouscommit = deserialize(previous);
			if (previouscommit.files() != null){
			    previouscommit.next.add(this);
			    for (File a : previouscommit.files()) {
			    	this.files.add(a);
			    }
			}

			if (newfiles != null) {
				for (File a : newfiles) {
					this.files.add(a);
				}
			}

			if (removefiles != null) {
				for (File a: removefiles) {
					if (this.files.contains(a)) {
						this.files.remove(a);
					}
				}
			}
		}
		this.date = createDate();
		this.serialize();
	}

	public String createDate(){
		Date today = new Date();
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        return date = DATE_FORMAT.format(today);
    }

    public String date(){
    	return date;
    }

	public int id() {
		return id;
	}

	public String message(){
		return message;
	}

	public HashSet<File> files() {
		return files;
	}

	public ArrayList nextCommit() {
		return next;
	}

	public CommitNode nextCommitAt(int childIndex) {
		return next.get(childIndex);
	}

	public int previousid() {
		return previous;
	}


	public void serialize() {
        try {
        	FileOutputStream fout = new FileOutputStream(".gitlet/" + id + "/commit.ser");
		 	ObjectOutputStream out = new ObjectOutputStream (fout);
		 	out.writeObject(this);
		 	out.flush();
		 	out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CommitNode deserialize(int id) {
        CommitNode c;
        try {
            FileInputStream fileIn = new FileInputStream(".gitlet/" + id + "/commit.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            c = (CommitNode) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            System.out.println("Commit object not found");
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            System.out.println("Commit class not found");
            e.printStackTrace();
            return null;
        }
        return c;
    }


}

