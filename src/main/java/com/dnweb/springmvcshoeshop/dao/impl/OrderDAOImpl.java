package com.dnweb.springmvcshoeshop.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.dnweb.springmvcshoeshop.dao.AccountDAO;
import com.dnweb.springmvcshoeshop.dao.OrderDAO;
import com.dnweb.springmvcshoeshop.dao.ProductDAO;
import com.dnweb.springmvcshoeshop.entities.Account;
import com.dnweb.springmvcshoeshop.entities.Order;
import com.dnweb.springmvcshoeshop.entities.OrderDetail;
import com.dnweb.springmvcshoeshop.entities.Product;
import com.dnweb.springmvcshoeshop.model.CartInfo;
import com.dnweb.springmvcshoeshop.model.CartLineInfo;
import com.dnweb.springmvcshoeshop.model.CustomerInfo;
import com.dnweb.springmvcshoeshop.model.OrderDetailInfo;
import com.dnweb.springmvcshoeshop.model.OrderInfo;
import com.dnweb.springmvcshoeshop.model.PaginationResult;

@Transactional
public class OrderDAOImpl implements OrderDAO {
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ProductDAO productDAO;
	
	@Autowired
	private AccountDAO accountDAO;

	private int getMaxOrderNum() {
		String sql = "Select max(o.orderNum) from " + Order.class.getName() + " o ";
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery(sql);
		Integer value = (Integer) query.uniqueResult();
		if (value == null) {
			return 0;
		}
		return value;
	}

	@Override
	public void saveOrder(CartInfo cartInfo) {
		Session session = sessionFactory.getCurrentSession();

		int orderNum = this.getMaxOrderNum() + 1;
		Order order = new Order();

		order.setId(UUID.randomUUID().toString());
		order.setOrdernum(orderNum);
		order.setCreated(new Date());
		order.setAmount(cartInfo.getAmountTotal());

		CustomerInfo customerInfo = cartInfo.getCustomerInfo();
		Account account = this.accountDAO.findAccount(customerInfo.getUsername());
		
		order.setAccount(account);

		session.persist(order);

		List<CartLineInfo> lines = cartInfo.getCartLines();

		for (CartLineInfo line : lines) {
			OrderDetail detail = new OrderDetail();
			
			detail.setId(UUID.randomUUID().toString());
			detail.setOrder(order);
			detail.setAmount(line.getAmount());
			detail.setPrice(line.getProductInfo().getPrice());
			detail.setQuantity(line.getQuantity());

			String code = line.getProductInfo().getId();
			Product product = this.productDAO.findProduct(code);
			detail.setProduct(product);

			session.persist(detail);
		}

		// Set OrderNum for report.
		// Set OrderNum để thông báo cho người dùng.
		cartInfo.setOrderNum(orderNum);
	}

	// @page = 1, 2, ...
	@Override
	public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage) {
		String sql = "Select new " + OrderInfo.class.getName()//
				+ "(ord.id, ord.orderDate, ord.orderNum, ord.amount, "
				+ " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone) " + " from "
				+ Order.class.getName() + " ord "//
				+ " order by ord.orderNum desc";
		Session session = this.sessionFactory.getCurrentSession();

		Query query = session.createQuery(sql);

		return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavigationPage);
	}

	public Order findOrder(String orderId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Order.class);
		crit.add(Restrictions.eq("id", orderId));
		return (Order) crit.uniqueResult();
	}

	@Override
	public OrderInfo getOrderInfo(String orderId) {
		Order order = this.findOrder(orderId);
		if (order == null) {
			return null;
		}
		return new OrderInfo(order.getId(), order.getAmount(), order.getCreated(), //
				order.getOrdernum(), order.getAccount().getName(), //
				order.getAccount().getAddress(), order.getAccount().getEmail(),
				order.getAccount().getPhone());
	}

	@Override
	public List<OrderDetailInfo> listOrderDetailInfos(String orderId) {
		String sql = "Select new " + OrderDetailInfo.class.getName() //
				+ "(d.id, d.product.id, d.product.name , d.quanity,d.price,d.amount) "//
				+ " from " + OrderDetail.class.getName() + " d "//
				+ " where d.order.id = :orderId ";

		Session session = this.sessionFactory.getCurrentSession();

		Query query = session.createQuery(sql);
		query.setParameter("orderId", orderId);

		return query.list();
	}
}
