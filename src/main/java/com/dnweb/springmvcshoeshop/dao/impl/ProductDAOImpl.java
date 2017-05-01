package com.dnweb.springmvcshoeshop.dao.impl;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.dnweb.springmvcshoeshop.dao.ProductDAO;
import com.dnweb.springmvcshoeshop.entities.Product;
import com.dnweb.springmvcshoeshop.model.PaginationResult;
import com.dnweb.springmvcshoeshop.model.ProductInfo;

@Transactional
public class ProductDAOImpl implements ProductDAO {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Product findProduct(String id) {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(Product.class);
		crit.add(Restrictions.eq("id", id));
		return (Product) crit.uniqueResult();
	}

	@Override
	public ProductInfo findProductInfo(String id) {
		Product product = this.findProduct(id);
		if (product == null) {
			return null;
		}
		return new ProductInfo(product);
	}

	@Override
	public void save(ProductInfo productInfo) {
		String id = productInfo.getId();

		Product product = null;

		boolean isNew = false;
		if (id != null) {
			product = this.findProduct(id);
		}
		if (product == null) {
			isNew = true;
			product = new Product();
			product.setCreated(new Date());
		}
		product.setId(id);
		product.setName(productInfo.getName());
		product.setPrice(productInfo.getPrice());

		if (productInfo.getFileData() != null) {
			byte[] image = productInfo.getFileData().getBytes();
			if (image != null && image.length > 0) {
				product.setImage(image);
			}
		}
		if (isNew) {
			this.sessionFactory.getCurrentSession().persist(product);
		}
		// If error in DB, Exceptions will be thrown out immediately
		// Nếu có lỗi tại DB, ngoại lệ sẽ ném ra ngay lập tức
		this.sessionFactory.getCurrentSession().flush();
	}

	@Override
	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage,
			String likeName) {
		String sql = "Select new " + ProductInfo.class.getName() //
				+ "(p.id, p.name, p.price) " + " from "//
				+ Product.class.getName() + " p ";
		if (likeName != null && likeName.length() > 0) {
			sql += " Where lower(p.name) like :likeName ";
		}
		sql += " order by p.created desc ";
		//
		Session session = sessionFactory.getCurrentSession();

		Query query = session.createQuery(sql);
		if (likeName != null && likeName.length() > 0) {
			query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
		}
		return new PaginationResult<ProductInfo>(query, page, maxResult, maxNavigationPage);
	}

	@Override
	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage) {
		return queryProducts(page, maxResult, maxNavigationPage, null);
	}
}
