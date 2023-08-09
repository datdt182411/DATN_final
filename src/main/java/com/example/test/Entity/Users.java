package com.example.test.Entity;

//import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Nationalized
//    @Column(unique = true)
    private String username;

    private String email;

//    private String name;


    @Column(unique = true)
    private String phone;
    private String password;
    private int status;
    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type", length = 10)
    private AuthenticationType authenticationType;

    @Column(name = "reset_password_token", length = 30)
    private String resetPasswordToken;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private List<Role> roleList;

    @OneToMany(mappedBy = "user")
    private List<Order> orderList;

    @OneToMany(mappedBy ="user" )
    private List<Review> reviewList;

    @OneToMany(mappedBy = "user")
    private List<ReviewVote> reviewVoteList;

    public boolean hasRole(String roleName) {
        Iterator<Role> iterator = roleList.iterator();

        while (iterator.hasNext()) {
            Role role = iterator.next();
            if (role.getName().equals(roleName)) {
                return true;
            }
        }

        return false;
    }
}
