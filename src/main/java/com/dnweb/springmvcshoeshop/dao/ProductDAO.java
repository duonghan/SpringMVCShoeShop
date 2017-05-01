package com.dnweb.springmvcshoeshop.dao;

import com.dnweb.springmvcshoeshop.entities.Product;
import com.dnweb.springmvcshoeshop.model.PaginationResult;
import com.dnweb.springmvcshoeshop.model.ProductInfo;

public interface ProductDAO {
	public Product findProduct(String id);

	public ProductInfo findProductInfo(String id);

	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage);

	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage, String likeName);

	public void save(ProductInfo productInfo);
}
