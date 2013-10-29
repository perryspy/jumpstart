package models;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.mindrot.jbcrypt.BCrypt;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;
import play.libs.Crypto;

import javax.persistence.*;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static play.data.validation.Constraints.Required;

/**
 * Created with IntelliJ IDEA for jumpstart
 * User: phutchinson
 * Date: 3/16/13
 * Time: 4:38 PM
 */
@Entity
@Table(name = "APP_USER")
public class User extends ModelBase {

    /***********************************************************************
     * Enums and Static fields                                             *
     ***********************************************************************/

    public enum RoleValue {
        STANDARD, ADMIN
    }


    /***********************************************************************
     * Class Member Variables                                              *
     ***********************************************************************/

    /**
     * The username that the user will use to log in to the system
     */
    @Required
    public String username;

    /**
     * encrypted password value
     */
    @Required
    public String password;

    @Transient
    @Constraints.MinLength(6)
    @Constraints.MaxLength(12)
    public String plainTextPassword;

    /**
     * encrypted password value the can be compared against the password to check that the user entered the same value
     * for both.  Not persisted
     */
    @Transient
    @Required
    public String confirmPassword;

    /**
     * A temporary password generated by the system as part of the reset password functionality.  Use this instead
     * of modifying the password directly so that people can't randomly reset other people's passwords.  A user can
     * be authenticated with the password or tempPassword if the tempPassword is used before the passwordExpiration date
     */
    public String temporaryPassword;

    /**
     * Date when the User's temporary password is no longer valid.
     */
    public Date temporaryPasswordExpiration;

    /**
     * A unique value that is used in the generation of passwords
     */
    public String salt = BCrypt.gensalt();

    /**
     * Timestamp of when the user last logged in to the application
     */
    @Temporal(TemporalType.TIMESTAMP)
    public Date lastLogin;

    /**
     * The number of times that someone has tried to login with this username with an incorrect password.  Should be
     * reset to zero upon successful login
     */
    public int failedLoginAttempts;

    /**
     * A list of roles that the User has
     */
    @ElementCollection
    @Enumerated(EnumType.STRING)
    public List<RoleValue> roles = new ArrayList<RoleValue>();


    /***********************************************************************
     * Persistence Operations                                              *
     ***********************************************************************/

    public static Finder<Long,User> find = new Finder<Long,User>(
            Long.class, User.class
    );


    /***********************************************************************
     * Login Handlers                                                      *
     ***********************************************************************/

    public static User connect(String username, String password){
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            throw new RuntimeException("error.usernameAndPasswordRequired");
        }

        User user = find.where().ieq("username", username).findUnique();

        //no user found
        if(user == null){
            throw new RuntimeException("error.userNotFound");
        }

        //password does not match
        if(!user.password.equals(user.hashPassword(password))) {
            user.failedLoginAttempts++;
            user.save();
            throw new RuntimeException("error.badPassword");
        }

        return user;
    }


    /***********************************************************************
     * Methods for handling password values                                *
     ***********************************************************************/

    /**
     * Sets the user's password to a hash of the plain text password argument and clears the password expiration.
     *
     * @param password Plain text password value.
     */
    public void setPassword(final String password) {
        this.plainTextPassword = password;
        this.password = hashPassword(password);
        //setting a new actual password clears out any temporary password data
        this.temporaryPassword = null;
        this.temporaryPasswordExpiration = null;
    }


    /**
     * Sets the user's password confirmation string to a hash of the plain text value.  Note that confirmPassword is a
     * transient value and will not be stored in the database
     * @param password
     */
    public void setConfirmPassword(final String password){
        this.confirmPassword = hashPassword(password);
    }

    /**
     * Sets a temporary password which is set to expire on the current date.
     *
     * @param password plain text password value.
     */
    public void setTemporaryPassword(final String password) {
        this.temporaryPassword = hashPassword(password);
        this.temporaryPasswordExpiration = DateUtils.addDays(new Date(), 1);
    }

    /**
     * Determines if the User has an expired temporary password
     * @return
     */
    public boolean temporaryPasswordExpired () {
        return temporaryPasswordExpiration != null && temporaryPasswordExpiration.before(new Date());
    }

    /**
     * Hash a password using the OpenBSD bcrypt scheme.  This can be used to check if a plain-text password matches the
     * encrypted and stored password value for this user.
     * @param password The plain-text password to be hashed
     * @return a String that is the hashed value of the password.
     */
    private String hashPassword(String password) {
        return Crypto.encryptAES(BCrypt.hashpw(password, salt));
    }


    /***********************************************************************
     * Validation Logic                                                    *
     ***********************************************************************/

    public List<ValidationError> validate(){
        List<ValidationError> errors = new ArrayList<ValidationError>();
        if(StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(confirmPassword) && !password.equals(confirmPassword)){
            errors.add(new ValidationError("password", "error.passwordAndConfirmPasswordNoMatch"));
        }

        List<User> duplicates = User.find.where().ilike("username", this.username).findList();
        if(duplicates.size () > 1 || duplicates.size() == 1 && !duplicates.contains(this)){
            errors.add(new ValidationError("username", "error.usernameNotUnique"));
        }

        //return null instead of empty list.
        //see: http://stackoverflow.com/questions/11388269/playframework-illegalstateexception-after-form-validation
        return errors.size() > 0 ? errors : null;
    }

}
