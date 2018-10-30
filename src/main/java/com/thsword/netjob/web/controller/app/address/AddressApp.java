package com.thsword.netjob.web.controller.app.address;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.thsword.netjob.dao.IAddressDao;
import com.thsword.netjob.global.Global;
import com.thsword.netjob.pojo.app.Address;
import com.thsword.netjob.service.IBaseService;
import com.thsword.netjob.util.ErrorUtil;
import com.thsword.netjob.util.JsonResponseUtil;
import com.thsword.utils.object.UUIDUtil;

@Controller
public class AddressApp {
	@Resource(name = "baseService")
	private IBaseService baseService;

	@RequestMapping("app/member/address/list")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String memberId = (String) request.getAttribute("memberId");
		Address address = new Address();
		address.setMemberId(memberId);
		address.setIsDel(false);
		@SuppressWarnings("unchecked")
		List<Address> addresses = (List<Address>) baseService.queryAllEntity(IAddressDao.class, address);
		JSONObject obj = new JSONObject();
		obj.put("list", addresses);
		JsonResponseUtil.successBodyResponse(addresses, response, request);
	}
	
	@RequestMapping("app/member/address/add")
	public void add(HttpServletRequest request, HttpServletResponse response,Address address) throws Exception {
		String memberId = (String) request.getAttribute("memberId");
		String province = address.getProvince();
		String citycode = address.getCitycode();
		String provinceName = address.getProvinceName();
		String cityName = address.getCityName();
		//String area = address.getArea();
		String detailAddress = address.getDetailAddress();
		String receiver = address.getReceiver();
		String phone = address.getPhone();
		Integer isDefault = address.getIsDefault();
		if(StringUtils.isEmpty(province)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "省code不能为空", response, request);
			return;
		}
		if(StringUtils.isEmpty(provinceName)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "省份名不能为空", response, request);
			return;
		}
		if(StringUtils.isEmpty(cityName)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "城市名不能为空", response, request);
			return;
		}
		if(StringUtils.isEmpty(citycode)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "城市code不能为空", response, request);
			return;
		}
		/*if(StringUtils.isEmpty(area)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "区域不能为空", response, request);
			return;
		}*/
		if(StringUtils.isEmpty(detailAddress)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "详细地址不能为空", response, request);
			return;
		}
		if(StringUtils.isEmpty(receiver)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "收件人不能为空", response, request);
			return;
		}
		if(StringUtils.isEmpty(phone)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "号码不能为空", response, request);
			return;
		}
		if(StringUtils.isEmpty(isDefault)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "是否默认不能为空", response, request);
			return;
		}
		if(isDefault==Global.SYS_MEMBER_ADDRESS_IS_DEFAULT_YES){
			Address temp = new Address();
			temp.setMemberId(memberId);
			temp.setIsDefault(isDefault);
			temp = (Address) baseService.queryEntity(IAddressDao.class,address);
			if(null!=temp){
				temp.setIsDefault(Global.SYS_MEMBER_ADDRESS_IS_DEFAULT_NO);
				baseService.updateEntity(IAddressDao.class,temp);
			}
		}
		address.setId(UUIDUtil.get32UUID());
		address.setMemberId(memberId);
		address.setCreateBy(memberId);
		address.setUpdateBy(memberId);
		baseService.addEntity(IAddressDao.class,address);
		JsonResponseUtil.successCodeResponse(response, request);
	}
	
	@RequestMapping("app/member/address/edit")
	public void edit(HttpServletRequest request, HttpServletResponse response,Address address) throws Exception {
		String id = address.getId();
		String memberId = (String) request.getAttribute("memberId");
		if(StringUtils.isEmpty(id)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "id不能为空", response, request);
			return;
		}
		if(!StringUtils.isEmpty(address.getIsDefault())&&address.getIsDefault()==Global.SYS_MEMBER_ADDRESS_IS_DEFAULT_YES){
			Address temp = new Address();
			temp.setMemberId(memberId);
			temp.setIsDefault(Global.SYS_MEMBER_ADDRESS_IS_DEFAULT_YES);
			temp = (Address) baseService.queryEntity(IAddressDao.class,address);
			if(null!=temp){
				temp.setIsDefault(Global.SYS_MEMBER_ADDRESS_IS_DEFAULT_NO);
				baseService.updateEntity(IAddressDao.class,temp);
			}
		}
		baseService.updateEntity(IAddressDao.class,address);
		JsonResponseUtil.successCodeResponse(response, request);
	}
	
	@RequestMapping("app/member/address/delete")
	public void delete(HttpServletRequest request, HttpServletResponse response,Address address) throws Exception {
		String id = address.getId();
		if(StringUtils.isEmpty(id)){
			JsonResponseUtil.msgResponse(ErrorUtil.HTTP_FAIL, "id不能为空", response, request);
			return;
		}
		address.setIsDel(true);
		baseService.deleteEntityById(IAddressDao.class,id);
		JsonResponseUtil.successCodeResponse(response, request);
	}
}
