package com.thsword.netjob.web.controller.app.comment;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.ICommentDao;
import com.thsword.netjob.pojo.app.Comment;
import com.thsword.netjob.service.CommentService;
import com.thsword.netjob.web.controller.base.BaseResponse;
import com.thsword.netjob.web.exception.ServiceException;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;

/**
 * 服务评论
 * 
 * @Description:TODO
 * 
 * @author:yong
 * 
 * @time:2018年5月10日 下午8:52:49
 */
@RestController
@Api(tags = "NETJOB-COMMENT", description = "评论接口")
public class CommentApp {
	@Resource(name = "commentService")
	CommentService commentService;

	/**
	 * 
	 * 
	 * 添加评论
	 * 
	 * @author: yong
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/comment/add")
	@ApiOperation(value = "添加评论", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "type", value = "类型（1-会员 2-服务 3-需求）", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "bizId", value = "业务ID", dataType = "string", paramType = "query", required = true) })
	public BaseResponse add(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String bizId,
			@RequestParam String content) throws Exception {
		try {
			String memberId = request.getAttribute("memberId") + "";
			Comment comment = new Comment();
			comment.setBizId(bizId);
			comment.setContent(content);
			comment.setId(UUIDUtil.get32UUID());
			comment.setMemberId(memberId);
			comment.setCreateBy(memberId);
			comment.setUpdateBy(memberId);
			commentService.addEntity(ICommentDao.class, comment);
			return BaseResponse.success();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 
	 * 
	 * 查看评论列表
	 * 
	 * @author: yong
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/comment/list")
	@ApiOperation(value = "查看评论列表", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "currentPage", value = "当前页", dataType = "int", paramType = "query", defaultValue = "1"),
			@ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", paramType = "query", defaultValue = "10"),
			@ApiImplicitParam(name = "bizId", value = "业务ID", dataType = "string", paramType = "query", required = true), })
	public JSONObject list(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam String bizId) throws Exception {
		Page page = new Page(currentPage, pageSize);
		Map<String, Object> map = new HashMap<String, Object>();
		page.setSort("t.c_createDate");
		page.setDir(Page.DIR_TYPE_DESC);
		map.put("page", page);
		map.put("bizId", bizId);
		@SuppressWarnings("unchecked")
		List<Comment> comments = (List<Comment>) commentService
				.queryPageEntity(ICommentDao.class, map);
		JSONObject result = new JSONObject();
		result.put("list", comments);
		result.put("page", page);
		return result;
	}

	/**
	 * 
	 * 
	 * 查看回复
	 * 
	 * @author: yong
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/comment/getReplys")
	@ApiOperation(value = "查看回复列表", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "currentPage", value = "当前页", dataType = "int", paramType = "query", defaultValue = "1"),
			@ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", paramType = "query", defaultValue = "10"),
			@ApiImplicitParam(name = "bizId", value = "业务ID", dataType = "string", paramType = "query", required = true), })
	public JSONObject getReplys(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false, defaultValue = "10") int pageSize,
			@RequestParam(required = false, defaultValue = "1") int currentPage,
			@RequestParam String parentId) throws Exception {
		Page page = new Page(currentPage, pageSize);
		Map<String, Object> map = new HashMap<String, Object>();
		page.setSort("t.c_createDate");
		page.setDir(Page.DIR_TYPE_DESC);
		map.put("page", page);
		map.put("parentId", parentId);
		@SuppressWarnings("unchecked")
		List<Comment> comments = (List<Comment>) commentService
				.queryPageEntity(ICommentDao.class, map);
		JSONObject result = new JSONObject();
		result.put("list", comments);
		result.put("page", page);
		return result;
	}

	/**
	 * 
	 * 
	 * 回复
	 * 
	 * @author: yong
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/member/comment/reply")
	@ApiOperation(value = "回复", httpMethod = "POST")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "parentId", value = "评论ID", dataType = "string", paramType = "query", required = true),
			@ApiImplicitParam(name = "content", value = "内容", dataType = "string", paramType = "query", required = true) })
	public BaseResponse reply(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String parentId,
			@RequestParam String content) throws Exception {
		String memberId = request.getAttribute("memberId") + "";
		Comment temp = (Comment) commentService.queryEntityById(
				ICommentDao.class, parentId);
		if (null == temp) {
			throw new ServiceException("该评论不存在");
		}
		Comment comment = new Comment();
		comment.setParentId(parentId);
		comment.setContent(content);
		comment.setMemberId(memberId);
		comment.setCreateBy(memberId);
		comment.setUpdateBy(memberId);
		comment.setId(UUIDUtil.get32UUID());
		commentService.addEntity(ICommentDao.class, comment);
		return BaseResponse.success();
	}

	/**
	 * 
	 * 
	 * 获取评论数
	 * 
	 * @author: yong
	 * 
	 * @time:2018年5月8日 上午12:07:45
	 */
	@RequestMapping("app/visitor/comment/counts")
	@ApiOperation(value = "评论数", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "bizId", value = "ID", dataType = "string", paramType = "query", required = true) })
	public int counts(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String bizId) throws Exception {
		return commentService.queryCountEntity(ICommentDao.class, bizId);
	}
}
