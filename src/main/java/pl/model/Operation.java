package pl.model;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "operation")
public class Operation implements Comparable<Operation>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne
    @JoinTable(name = "operation1_operation2", joinColumns = @JoinColumn(name = "operation1_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "operation2_id", referencedColumnName = "id"))
    private Operation operation;

    private Calendar creationDate;

    private Calendar nextUpdateDate;

    private int period;

    private int periodCount;

    private String label;

    private String category;

    private String description;

    private double amount;

    public Operation() {

    }

    public Operation(Calendar creationDate, Calendar nextUpdateDate, String label, String description, double amount) {
        this.creationDate = creationDate;
        this.nextUpdateDate = nextUpdateDate;
        this.label = label;
        this.description = description;
        this.amount = amount;
    }

    public Operation(Account account, Calendar creationDate, String label, String category, String description, double amount) {
        this.account = account;
        this.creationDate = creationDate;
        this.label = label;
        this.category = category;
        this.description = description;
        this.amount = amount;
    }

    public Operation(Account account, Calendar creationDate, Calendar nextUpdateDate, String label, String category, String description, double amount, int period) {
        this.account = account;
        this.creationDate = creationDate;
        this.nextUpdateDate = nextUpdateDate;
        this.label = label;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.period = period;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    public Calendar getNextUpdateDate() {
        return nextUpdateDate;
    }

    public void setNextUpdateDate(Calendar nextUpdateDate) {
        this.nextUpdateDate = nextUpdateDate;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getPeriodCount() {
        return periodCount;
    }

    public void setPeriodCount(int periodCount) {
        this.periodCount = periodCount;
    }

    public void incrementPeriodCount() {
        this.periodCount = this.periodCount + 1;
    }

    @Override
    public int compareTo(Operation operation) {
        return -getCreationDate().compareTo(operation.getCreationDate());
    }
}
