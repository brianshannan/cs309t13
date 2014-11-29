package edu.iastate.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.iastate.dao.GameDao;
import edu.iastate.models.Game;
import edu.iastate.models.Team;

@Controller
@RequestMapping("/game")
public class GameController {

    @RequestMapping(value = "/{id}/view", method = RequestMethod.GET)
    public String getGame(Model model, HttpSession session, @PathVariable int id) {
        model.addAttribute("gameId", id);
        if (session.getAttribute("member") == null) {
            return "redirect:denied";
        }

        GameDao gameDao = new GameDao();
        Game game = gameDao.getGameById(id, true);
        List<Team> teams = game.getTeams();
        model.addAttribute("game", game);
        model.addAttribute("teams", teams);
        return "game";
    }

    /**
     * Gets the game given by id as JSON.
     * 
     * @param id The id of the game.
     * @return JSON representation of the game.
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Game getGame(@PathVariable int id) {
        GameDao gameDao = new GameDao();
        return gameDao.getGameById(id, true);
    }
}
