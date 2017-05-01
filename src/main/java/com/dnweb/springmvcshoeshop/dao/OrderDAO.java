package com.dnweb.springmvcshoeshop.dao;

import java.util.List;

import com.dnweb.springmvcshoeshop.model.CartInfo;
import com.dnweb.springmvcshoeshop.model.OrderDetailInfo;
import com.dnweb.springmvcshoeshop.model.OrderInfo;
import com.dnweb.springmvcshoeshop.model.PaginationResult;

public interface OrderDAO {
	
	public void saveOrder(CartInfo cartInfo);

	public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage);

	public OrderInfo getOrderInfo(String orderId);

	public List<OrderDetailInfo> listOrderDetailInfos(String orderId);
}
