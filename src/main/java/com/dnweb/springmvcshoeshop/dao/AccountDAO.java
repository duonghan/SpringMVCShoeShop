package com.dnweb.springmvcshoeshop.dao;

import com.dnweb.springmvcshoeshop.entities.Account;

public interface AccountDAO {
	public Account findAccount(String userName);
}
