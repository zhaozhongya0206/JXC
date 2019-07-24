package com.mf.index.controller;

import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mf.index.service.IndexService;

@RestController
@RequestMapping("/admin/index")
public class IndexFindController {

	@Resource
	private IndexService indexService;
	
	@RequestMapping("/find")
	public Map<String,Object> findIndex() {
		return indexService.listIndex();
	}
	
}
