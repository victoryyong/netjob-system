package com.thsword.netjob.web.controller.app.comment;

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
	
	 * 添加评论

	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/comment/add")
	public void add(HttpServletRequest request, HttpServletResponse response,Comment comment) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			if (StringUtils.isEmpty(comment.getBizId())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "业务ID不能为空", response, request);
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
	
	 * 查看评论列表

	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/comment/list")
	public void list(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			String bizId = request.getParameter("bizId");
			if (StringUtils.isEmpty(bizId)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "ID不能为空", response, request);
				return;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			page.setSort("t.c_createDate");
			page.setDir(Page.DIR_TYPE_DESC);
			map.put("page", page);
			map.put("bizId", bizId);
			@SuppressWarnings("unchecked")
			List<Comment> comments = (List<Comment>) commentService.queryPageEntity(ICommentDao.class, map);
			JSONObject result = new JSONObject();
			result.put("list", comments);
			result.put("page", page);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	
	 * 查看回复

	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/comment/getReplys")
	public void getReplys(HttpServletRequest request, HttpServletResponse response,Page page) throws Exception {
		try {
			String parentId = request.getParameter("parentId");
			if (StringUtils.isEmpty(parentId)) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "父ID不能为空", response, request);
				return;
			}
			Map<String, Object> map = new HashMap<String, Object>();
			page.setSort("t.c_createDate");
			page.setDir(Page.DIR_TYPE_DESC);
			map.put("page", page);
			map.put("parentId", parentId);
			@SuppressWarnings("unchecked")
			List<Comment> comments = (List<Comment>) commentService.queryPageEntity(ICommentDao.class, map);
			JSONObject result = new JSONObject();
			result.put("list", comments);
			result.put("page", page);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	
	 * 回复

	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/comment/reply")
	public void reply(HttpServletRequest request, HttpServletResponse response,Comment comment) throws Exception {
		try {
			String memberId = request.getAttribute("memberId")+"";
			if (StringUtils.isEmpty(comment.getParentId())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "父ID不能为空", response, request);
				return;
			}
			if (StringUtils.isEmpty(comment.getContent())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "内容不能为空", response, request);
				return;
			}
			Comment temp = (Comment) commentService.queryEntityById(ICommentDao.class, comment.getParentId());
			if(null==temp){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "该评论不存在", response, request);
				return;
			}
			comment.setMemberId(memberId);
			comment.setCreateBy(memberId);
			comment.setUpdateBy(memberId);
			comment.setId(UUIDUtil.get32UUID());
			commentService.addEntity(ICommentDao.class, comment);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 
	
	 * 获取评论数

	 * @author: yong
	
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/comment/counts")
	public void counts(HttpServletRequest request, HttpServletResponse response,Comment comment) throws Exception {
		try {
			if (StringUtils.isEmpty(comment.getBizId())) {
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "ID不能为空", response, request);
				return;
			}
			int count =  commentService.queryCountEntity(ICommentDao.class, comment);
			JsonResponseUtil.successBodyResponse(count, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
