package jp.co.internous.origami.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.internous.origami.model.domain.dto.PurchaseHistoryDto;
import jp.co.internous.origami.model.mapper.TblPurchaseHistoryMapper;
import jp.co.internous.origami.model.session.LoginSession;

@Controller
@RequestMapping("/origami/history")
public class PurchaseHistoryController {
	
	@Autowired
	private LoginSession loginSession;
	@Autowired
	private TblPurchaseHistoryMapper tblPurchaseHistoryMapper;

	@RequestMapping("/")
	public String purchaseHistory(Model m) {
		int userId = loginSession.getUserId();
		List<PurchaseHistoryDto> historyList = tblPurchaseHistoryMapper.findByUserId(userId);
		m.addAttribute("historyList", historyList);
		m.addAttribute("loginSession",loginSession);
		
		return "purchase_history";
	}
	
	@RequestMapping("/delete")
	@ResponseBody
	public boolean delete() {
		int userId = loginSession.getUserId();
		int result = tblPurchaseHistoryMapper.logicalDeleteByUserId(userId);
	
		return result > 0;
	}
}
