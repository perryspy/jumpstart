package models;

import org.apache.commons.lang3.time.DateUtils;
import org.mindrot.jbcrypt.BCrypt;
import play.db.ebean.Model;
import play.libs.Crypto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA for jumpstart
 * User: phutchinson
 * Date: 3/16/13
 * Time: 4:38 PM
 */
@Entity
@Table(name = "APP_USER")
public class User extends Model {

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
    public String username;

    /**
     * encrypted password value
     */
    public String password;

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
     * Methods for handling password values                                *
     ***********************************************************************/

    /**
     * Sets the user's password to a hash of the plain text password argument and clears the password expiration.
     *
     * @param password Plain text password value.
     */
    public final void setPassword(final String password) {
        this.password = hashPassword(password);
        //setting a new actual password clears out any temporary password data
        this.temporaryPassword = null;
        this.temporaryPasswordExpiration = null;
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
    public String hashPassword(String password) {
        return Crypto.encryptAES(BCrypt.hashpw(password, salt));
    }
}
