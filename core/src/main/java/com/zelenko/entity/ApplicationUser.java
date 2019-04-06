package com.zelenko.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="application_users")
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationUser implements AbstractEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;
    @NotNull(message = "The field 'username' is mandatory")
    @Column(name = "user_name", nullable = false)
    private String userName;

    @NotNull(message = "The field 'password' is mandatory")
    @Column(name = "password", nullable = false)
    @ToString.Exclude
    private String password;
    @NotNull(message = "The field 'role' is mandatory")
    @Column(name = "role", nullable = false)
    @Builder.Default
    private String role = "USER";

    public ApplicationUser(@NotNull ApplicationUser applicationUser) {
        this.id = applicationUser.getId();
        this.userName = applicationUser.getUserName();
        this.password = applicationUser.getPassword();
        this.role = applicationUser.getRole();
    }
}
