package com.sft.shiro.credentials;

import com.sft.shiro.UserNamePasswordToken;
import com.sft.shiro.password.Encodes;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.util.ByteSource;

/**
 * 用户身份认证
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        if (info instanceof SaltedAuthenticationInfo) {
            ByteSource salt = ((SaltedAuthenticationInfo) info).getCredentialsSalt();
            if (token instanceof UserNamePasswordToken) {
                UserNamePasswordToken token2 = (UserNamePasswordToken) token;
                if (token2.isThirdLogin()) {
                    return true;
                }
                if (token2.getPassword() == null) {
                    return false;
                }
                return Encodes.validatePassword(new String(token2.getPassword()), (String) info.getCredentials());
            }
        }
        return false;
    }
}
