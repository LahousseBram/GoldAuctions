package com.qjewels.qjewels.model;

import com.qjewels.qjewels.model.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@Data
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    @Column(unique = true)
    private String email;

    private String passwordHash;

    private boolean termsAccepted;

    @Enumerated(EnumType.STRING)
    private Role userRole;

    private String emailVerificationToken;
    private boolean emailVerified = false;
    private LocalDateTime emailVerificationTokenExpiry;

    @Column(length = 512)
    private String refreshToken;

    @ToString.Exclude
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserProfile userProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Bid> bids;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AutoBid> autoBids;

    public void isTermsAccepted(boolean termsAccepted) {
        this.termsAccepted = termsAccepted;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "ROLE_" + userRole.name());
    }
}
