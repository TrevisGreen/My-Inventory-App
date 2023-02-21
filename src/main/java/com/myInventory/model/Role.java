package com.myInventory.model;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Objects;

@Entity
// Can cache this object, configuration for is under ehcache.xml
@Cacheable
// Defines table with the same 'roles
@Table(name = "roles")
public class Role implements Serializable, GrantedAuthority {

    // The id is the string authority and not a generated number

    @Id
    private String authority;

    // Default constructor
    public Role() {
    }

    // parameterized constructor
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    // returns hash, role or roles belonging to a user
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * Objects.hashCode(this.authority);
        return hash;
    }

    // returns true or false if authorities match
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Role other = (Role) obj;
        return Objects.equals(this.authority, other.authority);
    }

    // Returns a string of role or roles used by the user
    @Override
    public String toString() {
        return "Role{authority=" + authority + '}';
    }
}
