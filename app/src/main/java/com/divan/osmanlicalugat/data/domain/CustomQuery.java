package com.divan.osmanlicalugat.data.domain;

public class CustomQuery {
    String table;
    String[] columns;
    String selection;
    String[] selectionArgs;
    String groupBy;
    String having;
    String orderBy;
    String limit;

    public CustomQuery(String table, String columns[]){
        this.table = table;
        this.columns = columns;
    }

    public CustomQuery selection(String selection){
        this.selection = selection;
        return this;
    }

    public CustomQuery selectionArgs(String[] selectionArgs){
        this.selectionArgs = selectionArgs;
        return this;
    }

    public CustomQuery groupBy(String groupBy){
        this.groupBy = groupBy;
        return this;
    }

    public CustomQuery having(String having){
        this.having = having;
        return this;
    }

    public CustomQuery orderBy(String orderBy){
        this.orderBy = orderBy;
        return this;
    }

    public CustomQuery limit(String limit){
        this.limit = limit;
        return this;
    }

    public String getTable() {
        return table;
    }

    public String[] getColumns() {
        return columns;
    }

    public String getSelection() {
        return selection;
    }

    public String[] getSelectionArgs() {
        return selectionArgs;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public String getHaving() {
        return having;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getLimit() {
        return limit;
    }
}
