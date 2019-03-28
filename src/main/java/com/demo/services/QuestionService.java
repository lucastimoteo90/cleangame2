package com.demo.services;


import java.sql.Timestamp;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.domain.Answer;
import com.demo.domain.Question;
import com.demo.domain.Room;
import com.demo.domain.Score;
import com.demo.domain.Team;
import com.demo.domain.User;
import com.demo.dto.SkipDTO;
import com.demo.dto.TipDTO;
import com.demo.repositories.AnswerRepository;
import com.demo.repositories.QuestionRepository;
import com.demo.security.UserSS;
import com.demo.services.exception.AuthorizationException;




@Service
public class QuestionService {

	@Autowired
	private QuestionRepository repository;
	
	@Autowired
	private UserService userService;
	
	@Autowired 
	private ScoreService scoreService;
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private AnswerRepository answerRepository;
	
	public List<Question> findAll() {
		return repository.findAll();
	}

	public Question findById(Integer id) {
		return repository.findById(id).get();
	}
	
	public List<Question> findByRoom(Room room) {
		return repository.findByRoom(room);
	}
	
	public void insert(Question question) {
		repository.save(question);
	}

	public TipDTO getTip(Integer idQuestion, Integer idteam) {

		TipDTO tip = new TipDTO();
		
		UserSS userSS = UserService.authenticated();
		if(userSS == null ) {
			throw new AuthorizationException("Token Inválido");
		}
	
		User user = userService.findById(userSS.getID());
		Question question = repository.findById(idQuestion).get();
        Team team = teamService.findById(idteam);
		//Mudar pra team
		
		Answer answer = answerRepository.findByTeamAndQuestion(team, question).get(0);
		if(answer.getTips() < 2) {
		 answer.setTips(answer.getTips()+1);
		}
	    answerRepository.save(answer);	
		
	    tip.setQuestion_id(question.getId());
	    if(answer.getTips() == 1) {
	    	tip.setTip(question.getTip());
	    }else if(answer.getTips() == 2) {
	    	tip.setTip(question.getTip2());
	    }else {
	    	tip.setTip("No tips");
	    }
			return tip;
	}
	
	
	public TipDTO getTip1(Integer idQuestion, Integer idteam) {

		TipDTO tip = new TipDTO();
		
		UserSS userSS = UserService.authenticated();
		if(userSS == null ) {
			throw new AuthorizationException("Token Inválido");
		}
	
		User user = userService.findById(userSS.getID());
		Question question = repository.findById(idQuestion).get();
        Team team = teamService.findById(idteam);
		Score score = scoreService.findByUserAndRoom(user, question.getRoom()).get(0);
        //Mudar pra team
		
		
		
	    tip.setQuestion_id(question.getId());
	    
	    if(score.isGettip()) {
	      tip.setTip(question.getTip());
	      //Debitar pontos dica
	      Answer answer = answerRepository.findByTeamAndQuestion(team, question).get(0);
		  answer.setTip1(true);
		  answerRepository.save(answer);	
	    }else {
	    	tip.setTip("Você não pode solicitar nesse momento! Aguarde a próxima questão");
	    }
	    return tip;
	}
	
	public TipDTO getTip2(Integer idQuestion, Integer idteam) {

		TipDTO tip = new TipDTO();
		
		UserSS userSS = UserService.authenticated();
		if(userSS == null ) {
			throw new AuthorizationException("Token Inválido");
		}
	
		User user = userService.findById(userSS.getID());
		Question question = repository.findById(idQuestion).get();
        Team team = teamService.findById(idteam);
        Score score = scoreService.findByUserAndRoom(user, question.getRoom()).get(0);
		//Mudar pra team
		
		
		
	    tip.setQuestion_id(question.getId());
	    if(score.isGettip()) {
		    tip.setTip(question.getTip2());
	    	Answer answer = answerRepository.findByTeamAndQuestion(team, question).get(0);
			answer.setTip2(true);
			answerRepository.save(answer);	
	    }else{
		    	tip.setTip("Você não pode solicitar nesse momento! Aguarde a próxima questão");
		}
		return tip;
	}
	
	public TipDTO getTip3(Integer idQuestion, Integer idteam) {

		TipDTO tip = new TipDTO();
		
		UserSS userSS = UserService.authenticated();
		if(userSS == null ) {
			throw new AuthorizationException("Token Inválido");
		}
	
		User user = userService.findById(userSS.getID());
		Question question = repository.findById(idQuestion).get();
        Team team = teamService.findById(idteam);
        Score score = scoreService.findByUserAndRoom(user, question.getRoom()).get(0);
		//Mudar pra team
		
		
		
	    tip.setQuestion_id(question.getId());
	    if(score.isGettip()) {
		      tip.setTip(question.getTip3());
		      Answer answer = answerRepository.findByTeamAndQuestion(team, question).get(0);
			  answer.setTip2(true);
			  answerRepository.save(answer);	
	    }else{
		    	tip.setTip("Você não pode solicitar nesse momento! Aguarde a próxima questão");
		}
		return tip;
	}
	
	public SkipDTO skip(Integer idQuestion, Integer idteam) {

		SkipDTO skip = new SkipDTO();
		
		UserSS userSS = UserService.authenticated();
		if(userSS == null ) {
			throw new AuthorizationException("Token Inválido");
		}
	
		User user = userService.findById(userSS.getID());
		Question question = repository.findById(idQuestion).get();
		Team team = teamService.findById(idteam);
		
		System.out.println("Usuário: "+user.getName());
		System.out.println("Question: "+question.getAsk());
		System.out.println("Team: "+team.getId());
		
		Answer answer = answerRepository.findByTeamAndQuestion(team, question).get(0);
		System.out.println("Answer: "+answer.getQuestion().getAsk());
		answer.setSkip(true);
	    answer.setEnd(new Timestamp(System.currentTimeMillis()));
		answerRepository.save(answer);	
		
		
		if(scoreService.findByUserAndRoom(user, question.getRoom()).size() > 0 ){
			Score score = scoreService.findByUserAndRoom(user, question.getRoom()).get(0);
		    score.computeScore(answer);
		    scoreService.save(score);
		}else{
			Score score = new Score();
			score.setRoom(question.getRoom());
			score.setUser(user);
			score.setScore(0.0);
			score.setConsecutiveHits(0);
			score = scoreService.save(score);
			score.computeScore(answer);
			scoreService.save(score);
		}	 
		
	    
	    
	    skip.setQuestion_id(question.getId());
	    skip.setSkip(true);
		
		
		
		
		return skip;
	}
	
	

}
