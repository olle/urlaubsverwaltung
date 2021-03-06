package org.synyx.urlaubsverwaltung.core.person;

import com.google.common.base.MoreObjects;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import org.springframework.data.jpa.domain.AbstractPersistable;

import org.springframework.util.StringUtils;

import org.synyx.urlaubsverwaltung.security.Role;

import java.util.Collection;

import javax.persistence.*;


/**
 * This class describes a person.
 *
 * @author  Johannes Reuter
 * @author  Aljona Murygina
 */

@Entity
public class Person extends AbstractPersistable<Integer> {

    private static final long serialVersionUID = 765672310978437L;

    private String loginName;

    private String lastName;

    private String firstName;

    private String email;

    // private key of person - RSA
    // private key has to be saved as byte[] in database
    // when retrieved from database, byte[] have to be transformed back to private key
    @Column(columnDefinition = "longblob")
    private byte[] privateKey;

    // public key of person
    // saving like private key
    @Column(columnDefinition = "longblob")
    private byte[] publicKey;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    @Enumerated(EnumType.STRING)
    private Collection<Role> permissions;

    private boolean active;

    public Person() {

        /* OK */
    }


    public Person(String loginName, String lastName, String firstName, String email) {

        this.loginName = loginName;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
    }

    public boolean isActive() {

        return active;
    }


    public void setActive(boolean active) {

        this.active = active;
    }


    public String getEmail() {

        return email;
    }


    public void setEmail(String email) {

        this.email = email;
    }


    public String getFirstName() {

        return firstName;
    }


    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }


    public String getLastName() {

        return lastName;
    }


    public void setLastName(String lastName) {

        this.lastName = lastName;
    }


    public String getLoginName() {

        return loginName;
    }


    public void setLoginName(String loginName) {

        this.loginName = loginName;
    }


    public byte[] getPrivateKey() {

        return privateKey;
    }


    public void setPrivateKey(byte[] privateKey) {

        this.privateKey = privateKey;
    }


    public byte[] getPublicKey() {

        return publicKey;
    }


    public void setPublicKey(byte[] publicKey) {

        this.publicKey = publicKey;
    }


    public void setPermissions(Collection<Role> permissions) {

        this.permissions = permissions;
    }


    public Collection<Role> getPermissions() {

        return permissions;
    }


    public boolean hasRole(Role role) {

        for (Role r : getPermissions()) {
            if (r.equals(role)) {
                return true;
            }
        }

        return false;
    }


    public String getNiceName() {

        if (StringUtils.hasText(this.firstName) && StringUtils.hasText(this.lastName)) {
            return this.firstName + " " + this.lastName;
        }

        return this.loginName;
    }


    @Override
    public String toString() {

        MoreObjects.ToStringHelper toStringHelper = MoreObjects.toStringHelper(this);

        toStringHelper.add("id", getId());
        toStringHelper.add("loginName", getLoginName());
        toStringHelper.add("lastName", getLastName());
        toStringHelper.add("firstName", getFirstName());
        toStringHelper.add("email", getEmail());
        toStringHelper.add("permissions", getPermissions());

        return toStringHelper.toString();
    }
}
