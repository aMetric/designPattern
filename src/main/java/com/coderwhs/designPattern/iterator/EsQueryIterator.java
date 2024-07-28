package com.coderwhs.designPattern.iterator;

import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author whs
 * @Date 2024/7/28 17:34
 * @description: 具体迭代器
 */
public class EsQueryIterator implements Iterator<Map<String, Object>> {
    //记录当前cursor分页
    private String cursor;
    //记录查询的columnsList，因为只有第一次查询才会返回columnsList数据
    private List<String> columnsList;
    //将ES SQL Rest API的返回值封装到List<Map>中，以便处理返回值
    Iterator<Map<String, Object>> iterator;
    //此处我们从简而行，不再进行@Autowire注入，把更多的精力放到迭代器模式中
    RestTemplate restTemplate = new RestTemplate();

    //构造函数进行第一次查询，并且初始化我们后续需要使用的 columnsList 和 iterator 和 cursor
    public EsQueryIterator(String query, Long fetch_size) {
        EsResponseData esResponseData = restTemplate.postForObject("http://localhost:9200/_sql?format=json",
                new EsSqlQuery(query, fetch_size), EsResponseData.class);//第一次访问的结果出来了
        this.cursor = esResponseData.getCursor();
        this.columnsList = esResponseData.getColumns()
                .stream().map(x -> x.get("name"))
                .collect(Collectors.toList());
        this.iterator = convert(columnsList, esResponseData).iterator();
    }

    // hasNext 根据 是否 cursor 为null进行后续的 第二次，第三次，，，的访问，直到 cursor 为null
    @Override
    public boolean hasNext() {
        return iterator.hasNext() || scrollNext();
    }

    //获取第二次及以后的查询结果
    private boolean scrollNext() {
        if (iterator == null || this.cursor == null) {
            return false;
        }
        EsResponseData esResponseData = restTemplate.postForObject("http://localhost:9200/_sql?format=json",
                new EsSqlQuery(this.cursor), EsResponseData.class);
        this.cursor = esResponseData.getCursor();
        this.iterator = convert(columnsList, esResponseData).iterator();
        return iterator.hasNext();
    }

    @Override
    public Map<String, Object> next() {
        return iterator.next();
    }
    //将 ES SQL Rest API的返回值转化为 List<Map>
    private List<Map<String, Object>> convert(List<String> columnsList, EsResponseData esResponseData) {
        List<Map<String, Object>> results = new ArrayList<>();
        for (List<Object> row : esResponseData.getRows()) {
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < columnsList.size(); i++) {
                map.put(columnsList.get(i), row.get(i));
            }
            results.add(map);
        }
        return results;
    }
}
