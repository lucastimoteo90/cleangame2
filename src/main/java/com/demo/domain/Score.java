package com.demo.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.demo.domain.enums.RoomType;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Score")
public class Score {
    private static final long serialVersionUID = 1L;
    
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
    
    @ManyToOne
	@JoinColumn(name="user_id")
    @JsonIgnore
	private User user;
    
    @ManyToOne
	@JoinColumn(name="room_id")
    @JsonIgnore
	private Room room;
    
    private Integer consecutiveHits;
    
    private Double score;
    
    private Double panalites;
    
    public Score() {
    	this.setPanalites(0.0);
    }
        
    public void computeScore(Answer answer) {
        if(this.getRoom().getType()==RoomType.EASY){
    	  this.computeScoreEasy(answer);
    	}else {
    	   this.computeScoreMedium(answer);
    	} 
    }
    
    private void computeScoreEasy(Answer answer) {
    	if(answer.getSkip()){
        	//Pulou questão
        	this.setConsecutiveHits(0);        	
        }else if(!answer.getCorrect()) {
        	//Errou Questão
        	this.setConsecutiveHits(0);
        }else{
        	//Alternativa correta
        	this.setConsecutiveHits(this.getConsecutiveHits() + 1);          
        }
    }
    
    private void computeScoreMedium(Answer answer) {
        if(answer.getSkip()){
        	//Pulou questão
        	this.setConsecutiveHits(0);        	
        }else if(!answer.getCorrect()) {
        	//Errou Questão
        	this.setConsecutiveHits(0);
        	this.setScore(this.getScore() - 6);
        	
        }else{
        	//Alternativa correta
        	          
            
        	if(answer.getTip1()){
        		this.setScore(this.getScore() - 4);
        		this.setPanalites(this.getPanalites() + 4);
        	}
        	
        	if(answer.getTip2()) {
        		this.setScore(this.getScore() - 2);
        		this.setPanalites(this.getPanalites() + 2);
        	}
        	
        	if(answer.getTip3()) {
        		this.setScore(this.getScore() - 5);
        		this.setPanalites(this.getPanalites() + 5);
        	}
        	
        	//Calcula o tempo gasto na resposta
        	long diffSeconds = (answer.getEnd().getTime() - answer.getStart().getTime());
        	 if(diffSeconds < 60 ) {
        		this.setScore(this.getScore() + 3);
        	}else if(diffSeconds > 60 && diffSeconds < 80){
        		this.setScore(this.getScore() + 2);
        	}else if(diffSeconds > 80){
        		this.setScore(this.getScore() + 1);        		
        	}
        	 
        	//Pontuação acertos consecutivs
        	this.setScore(this.getScore() + this.getConsecutiveHits());
        	
        	this.setScore(this.getScore() + 12);//Pontuação da questão     	 
        	
        	this.setConsecutiveHits(this.getConsecutiveHits() + 1);
        	
        }        
        
    }
    
    

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Room getRoom() {
		return room;
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}
	
	

	public Double getPanalites() {
		return panalites;
	}

	public void setPanalites(Double panalites) {
		this.panalites = panalites;
	}

	public Integer getConsecutiveHits() {
		return consecutiveHits;
	}

	public void setConsecutiveHits(Integer consecutiveHits) {
		this.consecutiveHits = consecutiveHits;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}
	
	
	
    
	
	
}
