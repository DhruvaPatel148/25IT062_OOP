import java.io.*;

class Student implements Serializable {
    int id;
    String name;
    transient String password;

    Student(int id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }
}

public class SaveLoad {
    public static void main(String[] args) throws Exception {
        Student[] s = {
            new Student(1, "Dhruva", "abc123"),
            new Student(2, "Priya", "xyz456")
        };

        ObjectOutputStream out =
            new ObjectOutputStream(new FileOutputStream("student.dat"));
        out.writeObject(s);
        out.close();

        ObjectInputStream in =
            new ObjectInputStream(new FileInputStream("student.dat"));
        Student[] data = (Student[]) in.readObject();
        in.close();

        for (Student st : data) {
            System.out.println(st.id + " " + st.name + " " + st.password);
        }
    }
}