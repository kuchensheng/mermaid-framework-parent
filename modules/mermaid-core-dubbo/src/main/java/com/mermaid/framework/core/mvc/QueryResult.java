package com.mermaid.framework.core.mvc;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 结果分页处理
 * @author Chensheng.Ku
 * @version 创建时间：2018/8/31 14:56
 */
public class QueryResult<T> {
    private long total;
    private List<T> items;

    public QueryResult() {
    }

    public QueryResult(long total, List<T> items) {
        this.total = total;
        this.items = items;
    }

    public QueryResult(Page<T> page) {
        PageInfo pageInfo = page.toPageInfo();
        this.total = pageInfo.getTotal();
        this.items = pageInfo.getList();
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
