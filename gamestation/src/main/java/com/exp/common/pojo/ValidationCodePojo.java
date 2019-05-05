package com.exp.common.pojo;

public class ValidationCodePojo {

	private String code;
	private long expireDate;

	private ValidationCodePojo() {
		super();
	}

	public String getCode() {
		return this.code;
	}

	public long getExpireDate() {
		return this.expireDate;
	}

	public static ValidationCodePojo newInstance(String code, long expireDate) {
		ValidationCodePojo pojo = new ValidationCodePojo();
		pojo.code = code;
		pojo.expireDate = expireDate;
		return pojo;
	}

}
