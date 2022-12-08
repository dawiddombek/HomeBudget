package pl.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinTable(name = "account_userInfo", joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "userInfo_id", referencedColumnName = "id"))
    private UserInfo userInfo;

    @ManyToOne
    @JoinColumn(name = "home_id")
    private Home home;

    @OneToMany(mappedBy = "account")
    private Set<Operation> operations = new HashSet<>();

    private double currentAmount;

    public Account() {
    }

    public Account(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public Account(double currentAmount, Long id) {
        this.currentAmount = currentAmount;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setHome(Home home) {
        this.home = home;
    }

    public Home getHome() {
        return home;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Set<Operation> getOperations() {
        return operations;
    }

    public void setOperations(Set<Operation> operations) {
        this.operations = operations;
    }

    public void calculateCurrentAmount(double change) {
        this.currentAmount = this.currentAmount + change;
    }
}
