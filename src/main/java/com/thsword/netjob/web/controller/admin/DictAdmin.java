package com.thsword.netjob.web.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IDictDao;
import com.thsword.netjob.pojo.Dict;
import com.thsword.netjob.service.DictService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.object.UUIDUtil;
import com.thsword.utils.page.Page;
/**
 * 数据字典

 * @Description数据字典

 * @author:yong

 * @time:2018年5月8日 下午2:53:29
 */
@Controller
public class DictAdmin {
	@Resource(name="dictService")
	DictService dictService;
	@RequestMapping("admin/dict/list")
	public void getMapChild(HttpServletRequest request,HttpServletResponse response,Page page) throws Exception{
		try {
			JSONObject result  = new JSONObject();
			Map<String, Object> map = new HashMap<String, Object>();
			page.setSort("c_sort");
			page.setDir(Page.DIR_TYPE_ASC);
			map.put("page", page);
			List<Dict> permissions = dictService.queryPageRoots(map);
			result.put("page", page);
			result.put("list", permissions);
			JsonResponseUtil.successBodyResponse(result, response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	@RequestMapping("admin/dict/add")
	public void add(HttpServletRequest request,HttpServletResponse response,Dict dict) throws Exception{
		try {
			String userId = request.getAttribute("userId")+"";
			if(StringUtils.isEmpty(dict.getName())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "名称不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(dict.getSort())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "排序不能为空",response,request);
				return;
			}
			if(StringUtils.isEmpty(dict.getType())){
				if(StringUtils.isEmpty(dict.getValue())){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "value不能为空",response,request);
					return;
				}
				if(StringUtils.isEmpty(dict.getParentId())){
					JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "parentId不能为空",response,request);
					return;
				}
			}
			dict.setId(UUIDUtil.get32UUID());
			dict.setCreateBy(userId);
			dict.setUpdateBy(userId);
			dictService.addEntity(IDictDao.class, dict);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@RequestMapping("admin/dict/edit")
	public void edit(HttpServletRequest request,HttpServletResponse response,Dict dict) throws Exception{
		try {
			if(StringUtils.isEmpty(dict.getId())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "id不能为空",response,request);
				return;
			}
			dictService.updateEntity(IDictDao.class, dict);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@RequestMapping("admin/dict/delete")
	public void delete(HttpServletRequest request,HttpServletResponse response,Dict dict) throws Exception{
		try {
			if(StringUtils.isEmpty(dict.getId())){
				JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "id不能为空",response,request);
				return;
			}
			List<Dict> dicts = dictService.queryChilds(dict.getId());
			if(!CollectionUtils.isEmpty(dicts)){
				JsonResponseUtil.codeResponse(ErrorUtil.PERMISSION_HAS_CHILD, response,request);
				return;
			}
			dictService.updateEntity(IDictDao.class, dict);
			JsonResponseUtil.successCodeResponse(response, request);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
