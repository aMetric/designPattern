package com.coderwhs.designPattern.iterator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @Author whs
 * @Date 2024/7/28 17:33
 * @description: 具体容器
 */
@Data
@JsonIgnoreProperties
public class EsSqlQuery implements EsSqlQueryInterface<EsQueryIterator>{

    //插叙的sql语句
    private String query;
    //fetch_size参数
    private Long fetch_size;
    //游标
    private String cursor;


    //分页查询时候，传游标即可
    public EsSqlQuery(String cursor){
        this.cursor = cursor;
    }
    //第一次查询时候，传入querySql和fetch_size
    public EsSqlQuery(String query,Long fetch_size){
        this.query = query;
        this.fetch_size = fetch_size;
    }

    @Override
    public EsQueryIterator iterator() {
        return new EsQueryIterator(this.query,this.fetch_size);
    }
}
