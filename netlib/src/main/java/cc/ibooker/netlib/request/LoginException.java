package cc.ibooker.netlib.request;

import cc.ibooker.netlib.dto.ErrorData;

/**
 * 登录异常
 *
 * @author 邹峰立
 */
public class LoginException extends RunException {

    public LoginException(ErrorData errorData) {
        super(errorData);
    }
}
