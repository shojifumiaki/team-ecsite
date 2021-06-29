package jp.co.internous.origami.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.origami.model.domain.MstDestination;
import jp.co.internous.origami.model.mapper.MstDestinationMapper;
import jp.co.internous.origami.model.mapper.TblCartMapper;
import jp.co.internous.origami.model.mapper.TblPurchaseHistoryMapper;
import jp.co.internous.origami.model.session.LoginSession;

@Controller
@RequestMapping("/origami/settlement")
public class SettlementController {
	
	@Autowired
	private LoginSession loginSession;
	@Autowired
	private MstDestinationMapper mstDestinationMapper;
	
	private Gson gson = new Gson();
	
	@Autowired
	private TblCartMapper tblCartMapper;
	@Autowired
	private TblPurchaseHistoryMapper tblPurchaseHistoryMapper;
	
	@RequestMapping("/")
	public String settlement(Model m) {
		int userId = loginSession.getUserId();
		List<MstDestination> destinationList = mstDestinationMapper.findByUserId(userId);
		m.addAttribute("destinationList", destinationList);
		m.addAttribute("loginSession",loginSession);
		
		return "settlement";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/complete")
	@ResponseBody
	public boolean complete(@RequestBody String destinationId) {
		Map<String, String> map = gson.fromJson(destinationId, Map.class);
		String id = map.get("destinationId");
		
		int userId = loginSession.getUserId();
		Map<String, Object> parameter = new HashMap<>();
		parameter.put("destinationId", id);
		parameter.put("userId", userId);
		int insertCount = tblPurchaseHistoryMapper.insert(parameter);
		
		int deleteCount = 0;
		if (insertCount > 0) {
			deleteCount = tblCartMapper.deleteByUserId(userId);
		}
		return deleteCount == insertCount;
	}
}


