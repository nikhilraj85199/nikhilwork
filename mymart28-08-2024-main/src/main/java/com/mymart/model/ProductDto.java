package com.mymart.model;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;

public class ProductDto {

	@NotEmpty(message = "The name is  required")
	 private String name;
	 
	 @NotEmpty(message = "The name is required")
	 private String brand;
	 

	 private Category category;
	 
	 @Min(0)
	 private double price;
	 
	 @Size(min = 10, message = "The description should be atleast 10 characters")
	 @Size(max = 2000, message = "The description cannot exceed 2000 characters")
	 private String description;
	 
	 private MultipartFile imageFile;
	 private MultipartFile imageFile1;
	 private MultipartFile imageFile2;
	 private MultipartFile imageFile3;
	 private MultipartFile imageFile4;
	 
	 public MultipartFile getImageFile1() {
		return imageFile1;
	}

	public void setImageFile1(MultipartFile imageFile1) {
		this.imageFile1 = imageFile1;
	}

	public MultipartFile getImageFile2() {
		return imageFile2;
	}

	public void setImageFile2(MultipartFile imageFile2) {
		this.imageFile2 = imageFile2;
	}

	public MultipartFile getImageFile3() {
		return imageFile3;
	}

	public void setImageFile3(MultipartFile imageFile3) {
		this.imageFile3 = imageFile3;
	}

	public MultipartFile getImageFile4() {
		return imageFile4;
	}

	public void setImageFile4(MultipartFile imageFile4) {
		this.imageFile4 = imageFile4;
	}

	private int ratingCount = 0;
	 

	public int getRatingCount() {
		return ratingCount;
	}

	public void setRatingCount(int ratingCount) {
		this.ratingCount = ratingCount;
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

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}
	 
	 
	}
