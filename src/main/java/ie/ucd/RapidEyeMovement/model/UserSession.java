package ie.ucd.RapidEyeMovement.model;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserSession
{
    private User user;
    private boolean loginFailed;
    private boolean wrongRole;

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public boolean isLoginFailed()
    {
        return loginFailed;
    }

    public void setLoginFailed(boolean loginFailed)
    {
        this.loginFailed = loginFailed;
    }

    public boolean isWrongRole()
    {
        return wrongRole;
    }

    public void setWrongRole(boolean wrongRole)
    {
        this.wrongRole = wrongRole;
    }
}