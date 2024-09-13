package com.mymart.model;

import org.springframework.web.multipart.MultipartFile;

public class carouselDto
{

	private MultipartFile imageFile;

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}
	
	
	
}
