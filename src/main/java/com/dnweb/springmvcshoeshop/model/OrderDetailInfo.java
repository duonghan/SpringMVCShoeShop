package com.dnweb.springmvcshoeshop.model;

public class OrderDetailInfo {
	
	private String id;
	
	private String productid;
	private String productname;
	private double price;
	
	private int quantity;
	private double amount;
	
	public OrderDetailInfo() {
		
	}

	public OrderDetailInfo(String id, String productid, String productname, double price, int quantity, double amount) {
		super();
		this.id = id;
		this.productid = productid;
		this.productname = productname;
		this.price = price;
		this.quantity = quantity;
		this.amount = amount;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public String getProductname() {
		return productname;
	}

	public void setProductname(String productname) {
		this.productname = productname;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
}
