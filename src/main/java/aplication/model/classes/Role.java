package aplication.model.classes;

import javax.persistence.*;

@Entity
@Table(name="role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="role_id")
    private Long id;

    private String name;

    public Role() {

    }

    public Role(String  roleName) {
        this.name = roleName;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
