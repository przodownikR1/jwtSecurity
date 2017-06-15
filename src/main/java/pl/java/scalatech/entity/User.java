package pl.java.scalatech.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="users")
@ToString(exclude = { "password" })
public class User extends AbstactId {
    private static final long serialVersionUID = -2181703844979860927L;

    @NotNull
    @Size(min = 2, max = 30)
    private String login;

    @JsonIgnore
    private String password;
    
    
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER,cascade={CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(uniqueConstraints=@UniqueConstraint(columnNames = { "userId", "roleId" }),name = "USER_ROLE", joinColumns = { @JoinColumn(name = "userId") }, inverseJoinColumns = { @JoinColumn(name = "roleId") })
    private List<Role> roles;
}