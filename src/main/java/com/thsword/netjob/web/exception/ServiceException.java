package com.thsword.netjob.web.exception;

import com.thsword.netjob.util.ErrorUtil;

public class ServiceException extends RuntimeException{
	public ServiceException() {
		super();
	}
    public ServiceException(String message) {
        super(message);
    }
    public ServiceException(int code) {
        super(ErrorUtil.getMessage(code));
    }
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
    public ServiceException(Throwable cause) {
        super(cause);
    }
}
