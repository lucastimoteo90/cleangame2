package com.demo.dto;

import java.io.Serializable;

public class ResumeRoomDTO implements Serializable {
  	private static final long serialVersionUID = 1L;

	Integer room_id;
    
    Integer hits;
    Integer errors;
    Integer skips;
    
    Integer totalQuestions;

	public Integer getRoom_id() {
		return room_id;
	}

	public void setRoom_id(Integer room_id) {
		this.room_id = room_id;
	}

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	public Integer getErrors() {
		return errors;
	}

	public void setErrors(Integer errors) {
		this.errors = errors;
	}

	public Integer getTotalQuestions() {
		return totalQuestions;
	}

	public void setTotalQuestions(Integer totalQuestions) {
		this.totalQuestions = totalQuestions;
	}

	public Integer getSkips() {
		return skips;
	}

	public void setSkips(Integer skips) {
		this.skips = skips;
	}
    
    
    
    
    
    
}
