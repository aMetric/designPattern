package com.coderwhs.designPattern.controller;

import com.coderwhs.designPattern.iterator.EsSqlQuery;
import com.coderwhs.designPattern.service.inter.EsQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/es")
public class EsQueryController {
    @Autowired
    private EsQueryService esQueryService;
    @PostMapping("/queryBySql")
    public Object queryEsBySql(@RequestBody EsSqlQuery esSqlQuery) {
        return esQueryService.queryEsBySql(esSqlQuery);
    }
}
