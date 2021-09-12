package com.dell.webservice.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "eorder")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "eproduct_id")
	private int id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "totalPrice")
	private double totalPrice;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "address")
	private String address;
	
	@Column(name = "phoneNo")
	private String phoneNo;
	
	@OneToMany(cascade=CascadeType.MERGE, fetch=FetchType.LAZY)
	private Set<Product> products;

	public Order() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Order(String name, double totalPrice, String email, String address, String phoneNo, Set<Product> products) {
		super();
		this.name = name;
		this.totalPrice = totalPrice;
		this.email = email;
		this.address = address;
		this.phoneNo = phoneNo;
		this.products = products;
	}

	public Order(int id, String name, double totalPrice, String email, String address, String phoneNo,
			Set<Product> products) {
		super();
		this.id = id;
		this.name = name;
		this.totalPrice = totalPrice;
		this.email = email;
		this.address = address;
		this.phoneNo = phoneNo;
		this.products = products;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public Set<Product> getProducts() {
		return products;
	}

	public void setProducts(Set<Product> products) {
		this.products = products;
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", name=" + name + ", totalPrice=" + totalPrice + ", email=" + email + ", address=" + address
				+ ", phoneNo=" + phoneNo + "]";
	}
}
