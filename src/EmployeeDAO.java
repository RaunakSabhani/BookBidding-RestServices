import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;


public class EmployeeDAO {
    
    public void addEmployee(Employee bean){
		EntityManager entityManager = EntityManagerUtil.getManager().createEntityManager();
	    entityManager.getTransaction().begin();
        Employee employee = new Employee();
        
        employee.setName(bean.getName());
        employee.setAge(bean.getAge());

        entityManager.persist( employee );

	    entityManager.getTransaction().commit();

	    // get a new EM to make sure data is actually retrieved from the store and not Hibernate's internal cache
	    entityManager.close();
        
    }
        
    /*private void addEmployee(Session session, Employee bean){
        Employee employee = new Employee();
        
        employee.setName(bean.getName());
        employee.setAge(bean.getAge());
        
        session.save(employee);
    }*/
    
    public List<Employee> getEmployees(){
		EntityManager entityManager = EntityManagerUtil.getManager().createEntityManager();
	    entityManager.getTransaction().begin();    
        Query query = entityManager.createQuery("from Employee");
        List<Employee> employees =  query.getResultList();
        return employees;
    }
 
    /*public int deleteEmployee(int id) {
        Session session = SessionUtil.getSession();
        Transaction tx = session.beginTransaction();
        String hql = "delete from Employee where id = :id";
        Query query = session.createQuery(hql);
        query.setInteger("id",id);
        int rowCount = query.executeUpdate();
        System.out.println("Rows affected: " + rowCount);
        tx.commit();
        session.close();
        return rowCount;
    }
    
    public int updateEmployee(int id, Employee emp){
         if(id <=0)  
               return 0;  
         Session session = SessionUtil.getSession();
            Transaction tx = session.beginTransaction();
            String hql = "update Employee set name = :name, age=:age where id = :id";
            Query query = session.createQuery(hql);
            query.setInteger("id",id);
            query.setString("name",emp.getName());
            query.setInteger("age",emp.getAge());
            int rowCount = query.executeUpdate();
            System.out.println("Rows affected: " + rowCount);
            tx.commit();
            session.close();
            return rowCount;
    }*/
}
