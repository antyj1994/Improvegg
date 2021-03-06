package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.*;

import model.Champion;
import model.Item;
import model.Spell;
import model.jsp.Partita;

import net.rithms.riot.api.ApiConfig;
import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.api.endpoints.match.dto.Match;
import net.rithms.riot.api.endpoints.match.dto.MatchList;
import net.rithms.riot.api.endpoints.match.dto.MatchReference;
import net.rithms.riot.api.endpoints.match.dto.Participant;
import net.rithms.riot.api.endpoints.match.dto.ParticipantStats;
import net.rithms.riot.api.endpoints.match.dto.Rune;
import net.rithms.riot.api.endpoints.match.dto.TeamStats;
import net.rithms.riot.api.endpoints.static_data.dto.Passive;
import net.rithms.riot.api.endpoints.summoner.dto.Summoner;
import net.rithms.riot.constant.Platform;
import persistence.DAOFactory;
import persistence.DatabaseManager;
import persistence.dao.ChampionDao;
import persistence.dao.FavouriteDao;
import persistence.dao.ItemDao;
import persistence.dao.SpellDao;

public class ExpandMatches extends HttpServlet{
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		PrintWriter out = resp.getWriter();
		Gson gson  = new Gson();
		try {
			
			ApiConfig config = new ApiConfig().setKey("RGAPI-f118efd6-5b54-4552-b609-e337d8fee576");
			RiotApi api = new RiotApi(config);
			String name = req.getParameter("summonerName");
			int time = Integer.parseInt(req.getParameter("timess"));
			Summoner summoner = null;
			boolean trovato = true;
			Platform pl = Platform.EUW;
			try {
				summoner = api.getSummonerByName(pl, name);
			}catch(RiotApiException e) {
				e.printStackTrace();
			}
			if (trovato) {
				long id = summoner.getAccountId();
				MatchList ml = null;
				boolean matchTrovati = true;
				
				try {
					ml = api.getRecentMatchListByAccountId(pl, id); //GETS THE RECENT MATCHLIST  BUG #1
				} catch (RiotApiException e) {
					req.setAttribute("emptyMatches", true);
					req.setAttribute("causa", "Sorry! We currently can't retrieve your matches");
					matchTrovati = false;
					e.printStackTrace();
				}
				if (matchTrovati) {
					List<MatchReference> l = ml.getMatches();
					List<Partita> partite = new LinkedList<>();
					Partita partita = null;
					int onlyLast = 0;
					
					for(Iterator<MatchReference> it = l.iterator(); it.hasNext() && onlyLast < 10+(5*time);){
						onlyLast++;
						if(onlyLast <= 5 + (5*time)) {
							it.next();
						}
						else {
						MatchReference m = it.next();
						
						long gameId = m.getGameId();
						Match match = api.getMatch(pl, gameId); //GET THE MATCH
						
						Participant part = match.getParticipantByAccountId(id); //GET THE PLAYER
						ParticipantStats ps = part.getStats(); //GET THE PLAYER STATS
						
						String wonOrLost = null;
						List<TeamStats> lts = match.getTeams();
						for (TeamStats ts : lts) {
							if (ts.getTeamId() == part.getTeamId()) {
								wonOrLost = ts.getWin();
							}
						}
						
						
						DAOFactory daoFactory = DatabaseManager.getInstance().getDaoFactory();
						ChampionDao championDao = daoFactory.getChampionDAO();
						Champion champion = championDao.findByPrimaryKey(part.getChampionId());
						
						List<String> champs = new ArrayList<>();
						for(Participant p: match.getParticipants()) {
							Champion champion2 = (championDao.findByPrimaryKey(p.getChampionId()));
							champs.add(champion2.getUrl());
						}
						
						ItemDao itemDao = daoFactory.getItemDAO();
						Item item0 = itemDao.findByPrimaryKey(ps.getItem0());
						Item item1 = itemDao.findByPrimaryKey(ps.getItem1());
						Item item2 = itemDao.findByPrimaryKey(ps.getItem2());
						Item item3 = itemDao.findByPrimaryKey(ps.getItem3());
						Item item4 = itemDao.findByPrimaryKey(ps.getItem4());
						Item item5 = itemDao.findByPrimaryKey(ps.getItem5());
						SpellDao spellDao = daoFactory.getSpellDAO();
						Spell spell1 = spellDao.findByPrimaryKey(part.getSpell1Id());
						Spell spell2 = spellDao.findByPrimaryKey(part.getSpell2Id());
						
						partita = new Partita();
						partita.setChamps(champs);
						partita.setGameMode(match.getGameMode());
						partita.setResult(wonOrLost);
						partita.setGameDuration(match.getGameDuration());
						
						partita.setChampName(champion.getNome());
						partita.setChampLevel(ps.getChampLevel());
						partita.setChamp(champion.getTooltip());
						
						partita.setKda(ps.getKills()+"/"+ps.getDeaths()+"/"+ps.getAssists());
						partita.setGolds(ps.getGoldEarned());
						partita.setCs(ps.getTotalMinionsKilled());
						
						partita.setVisionScore((int)ps.getVisionScore());
						
						partita.setLane(m.getLane());
						
						
						partita.setChampUrl(champion.getUrl());
						partita.setSpellUrl1(spell1.getUrl());
						partita.setSpellUrl2(spell2.getUrl());
						
						partita.setSpell1(spell1.getTooltip());
						partita.setSpell2(spell2.getTooltip());
						
						if (item0!=null) {
						partita.setItemUrl0(item0.getUrl());
						partita.setItem0(item0.getTooltip());
						}
						else {
							partita.setItemUrl0("items//No_item.png");
							partita.setItem0("items//No_item.png");
						}
						
						if (item1!=null) {
							partita.setItemUrl1(item1.getUrl());
							partita.setItem1(item1.getTooltip());
						}
						else {
							partita.setItemUrl1("items//No_item.png");
							partita.setItem1("items//No_item.png");
						}
						
						if(item2!=null) {
							partita.setItemUrl2(item2.getUrl());
							partita.setItem2(item2.getTooltip());
						}
						else {
							partita.setItemUrl2("items//No_item.png");
							partita.setItem2("items//No_item.png");
						}
						
						if(item3!=null) {
							partita.setItemUrl3(item3.getUrl());
							partita.setItem3(item3.getTooltip());
						}
						else {
							partita.setItemUrl3("items//No_item.png");
							partita.setItem3("items//No_item.png");
						}
						
						if(item4!=null) {
							partita.setItemUrl4(item4.getUrl());
							partita.setItem4(item4.getTooltip());
						}
						else {
							partita.setItemUrl4("items//No_item.png");
							partita.setItem4("items//No_item.png");
						}
						
						if(item5!=null) {
							partita.setItemUrl5(item5.getUrl());
							partita.setItem5(item5.getTooltip());
						}
						else {
							partita.setItemUrl5("items//No_item.png");
							partita.setItem5("items//No_item.png");
						}
						
						
						partite.add(partita);
					}//else
					}//for
					out.println(gson.toJson(partite));
					out.close();
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}	
