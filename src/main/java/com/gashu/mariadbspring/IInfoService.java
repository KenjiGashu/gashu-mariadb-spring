package com.gashu.mariadbspring;

import org.springframework.security.access.prepost.PreAuthorize;
public interface IInfoService {
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
	public String getMsg();
} 
