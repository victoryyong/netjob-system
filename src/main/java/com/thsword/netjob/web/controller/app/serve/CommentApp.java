package com.thsword.netjob.web.controller.app.serve;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.ICommentDao;
import com.thsword.netjob.pojo.app.Comment;
import com.thsword.netjob.service.CommentService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;
/**
 * 服务评论

 * @Description:TODO

 * @author:yong

 * @time:2018年5月10日 下午8:52:49
 */
@Controller
public class CommentApp {
	@Resource(name = "commentService")
	CommentService commentService;

	/**
	 * 
	
	 * 添加评论S

	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/serve/comment/add")
	public void add(HttpServletRequest request, HttpServletResponse response,Comment comment) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			if (StringUtils.isEmpty(comment.getServeId())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "评论服务ID不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(comment.getContent())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "评论内容不能为空", response, request);
				return;
			}
			comment.setId(UUIDUtil.get32UUID());
			comment.setMemberId(memberId);
			comment.setCreateBy(memberId);
			comment.setUpdateBy(memberId);
			commentService.addEntity(ICommentDao.class, comment);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	
	 * 添加需求

	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/serve/comment/list")
	public void list(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			String serveId = request.getParameter("serveId");
			if (StringUtils.isEmpty(serveId)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "服务ID不能为空", response, request);
				return;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			page.setSort("t.c_createDate");
			page.setDir(Page.DIR_TYPE_DESC);
			map.put("page", page);
			map.put("serveId", serveId);
			@SuppressWarnings("unchecked")
			List<Comment> serves = (List<Comment>) commentService.queryPageEntity(ICommentDao.class, map);
			JSONObject result = new JSONObject();
			result.put("list", serves);
			result.put("page", page);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
