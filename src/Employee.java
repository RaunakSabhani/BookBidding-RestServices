import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Employee {
 
    @Id 
    @GeneratedValue
    private Long id;
    
    //
    @Column 
    private String name;
    @Column
    private int age;
    
    /*public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }*/
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    
}
