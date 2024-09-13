package com.mymart.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="products")
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;
	private String brand;
	
	private double price;
	
	private int ratingCount = 0;
	
	
	public int getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(int ratingCount) {
		this.ratingCount = ratingCount;
	}


	@Column(columnDefinition="TEXT")
	private String description;
	private Date createdAt;
	

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Rating> ratings = new ArrayList<>();

  
    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    
    public void addRating(Rating rating) {
        ratings.add(rating);
        rating.setProduct(this);
    }

    public void removeRating(Rating rating) {
        ratings.remove(rating);
        rating.setProduct(null);
    }
	
	
	
	
	@ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
	
	private String imageFileName;
	private String imageFileName1;
	private String imageFileName2;
	private String imageFileName3;
	private String imageFileName4;
	
	
	public Product(int id, String name, String brand, double price, int ratingCount, String description, Date createdAt,
			List<Rating> ratings, Category category, String imageFileName, String imageFileName1, String imageFileName2,
			String imageFileName3, String imageFileName4, double discountedPrice, Deal deal) {
		super();
		this.id = id;
		this.name = name;
		this.brand = brand;
		this.price = price;
		this.ratingCount = ratingCount;
		this.description = description;
		this.createdAt = createdAt;
		this.ratings = ratings;
		this.category = category;
		this.imageFileName = imageFileName;
		this.imageFileName1 = imageFileName1;
		this.imageFileName2 = imageFileName2;
		this.imageFileName3 = imageFileName3;
		this.imageFileName4 = imageFileName4;
		this.discountedPrice = discountedPrice;
		this.deal = deal;
	}




	private double discountedPrice;
	
	 public String getImageFileName1() {
		return imageFileName1;
	}

	public void setImageFileName1(String imageFileName1) {
		this.imageFileName1 = imageFileName1;
	}

	public String getImageFileName2() {
		return imageFileName2;
	}

	public void setImageFileName2(String imageFileName2) {
		this.imageFileName2 = imageFileName2;
	}

	public String getImageFileName3() {
		return imageFileName3;
	}

	public void setImageFileName3(String imageFileName3) {
		this.imageFileName3 = imageFileName3;
	}

	public String getImageFileName4() {
		return imageFileName4;
	}

	public void setImageFileName4(String imageFileName4) {
		this.imageFileName4 = imageFileName4;
	}




	@OneToOne(mappedBy = "product")
	    private Deal deal;
	
	 
	 
	public Deal getDeal() {
		return deal;
	}

	public double getDiscountedPrice() {
        if (deal != null) {
            double discountPercentage = deal.getDiscount();
            return price * (1 - discountPercentage / 100);
        } else {
            return price; // If no deal is associated, return the original price
        }
    }
	
	public void calculateDiscountedPrice() {
        if (deal != null) {
            double discountPercentage = deal.getDiscount();
            this.discountedPrice = price * (1 - discountPercentage / 100);
        } else {
            this.discountedPrice = price; // If no deal is associated, set discounted price as original price
        }
    }

	
	public void updateDiscountedPrice() {
        if (deal != null) {
            // Calculate discounted price based on deal
            double discount = deal.getDiscount();
            discountedPrice = price - (price * (discount / 100));
        } else {
            // If no deal is associated, set discounted price same as price
            discountedPrice = price;
        }
    }
	public void setDiscountedPrice(double discountedPrice) {
		this.discountedPrice = discountedPrice;
	}

	public void setDeal(Deal deal) {
		this.deal = deal;
	}


	public Product(int id, String name, String brand, double price, String description, Date createdAt, Category category,
		String imageFileName, int ratingCount) {
	super();
	this.id = id;
	this.name = name;
	this.brand = brand;
	this.price = price;
	this.description = description;
	this.createdAt = createdAt;
	this.category = category;
	this.imageFileName = imageFileName;
	this.ratingCount = ratingCount;
}


	public int getId() {
		return id;
	}
	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", brand=" + brand + ", price=" + price + ", ratingCount="
				+ ratingCount + ", description=" + description + ", createdAt=" + createdAt + ", ratings=" + ratings
				+ ", category=" + category + ", imageFileName=" + imageFileName + ", imageFileName1=" + imageFileName1
				+ ", imageFileName2=" + imageFileName2 + ", imageFileName3=" + imageFileName3 + ", imageFileName4="
				+ imageFileName4 + ", discountedPrice=" + discountedPrice + ", deal=" + deal + "]";
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
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public Product() {
		super();
		
	}

	
}