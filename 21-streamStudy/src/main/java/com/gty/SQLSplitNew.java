package com.gty;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.*;
import java.util.stream.Collectors;

public class SQLSplitNew {
    private static final String SELECT_STR = " SELECT ";
    private static final String AND_STR = " AND ";
    public static final String LIKE = "like";
    public static final String IN = "in";
    public static final String NOT_IN = "not in";
    public static final String EQ = "=";
    public static final String BETWEEN = "between";
    public static final String SPACE = " ";
    public static final String ALL_FIELD = " * ";
    public static final String AS = " AS ";
    public static final String ONE_EQ_ONE = " 1 = 1 ";
    public static final String ASC = "ascending";


    public static String toSelectSQL(Boolean needCount, Boolean isSummery, String tableName,
                                     Map<String, String> fieldMap, Map<String, String> groupByMap) {
        return toSelectSQL(needCount, isSummery, tableName, fieldMap, groupByMap, null, null, null, null);
    }


    /**
     * @param needCount  是否求总数
     * @param isSummery  是否求和
     * @param tableName  表名
     * @param fieldMap   字段映射
     * @param groupByMap 这里key为前端别名,value可以为空, 当需要使用group by字段拼接select查询字段的时候,格式为value as key,当需要拼接group by时,则只取key,这里注意请使用Map,hutools的JsonObject会默认忽略null值,导致group 缺失,如果需要key有序请使用LinkedHashMap
     * @param timeRange  这里除了since和unit还需要配置一下数据库的时间字段,有表使用的是day有些是date,time_range.set(JSONKeyNames.TIME_RANG_FIELD,"date")
     * @param whereList  过滤
     * @param orderBy    排序
     * @param limitPage  分页
     * @return
     */
    public static String toSelectSQL(Boolean needCount, Boolean isSummery, String tableName,
                                     Map<String, String> fieldMap, Map<String, String> groupByMap,
                                     JSONObject timeRange, JSONArray whereList, JSONObject orderBy, JSONObject limitPage) {
        StringBuilder sql = new StringBuilder(SELECT_STR);

        //将aliasOrFormulaMap复制一份
        HashMap<String, String> aliasCopyMap = CollectionUtil.<String, String>newHashMap(15, true);

        //是否需要条数,只支持postgre
        if (needCount) {
            aliasCopyMap.put("total_count", "COUNT(1) OVER()");
        }

        if (!isSummery && CollectionUtil.isNotEmpty(groupByMap)) {
            aliasCopyMap.putAll(groupByMap);
        }

        if (CollectionUtil.isNotEmpty(fieldMap)) {
            aliasCopyMap.putAll(fieldMap);
        }

        //处理fields
        if (CollectionUtil.isNotEmpty(aliasCopyMap)) {
            ArrayList<String> fieldList = CollectionUtil.newArrayList();
            aliasCopyMap.forEach((key, value) -> {
                if (StrUtil.isBlankOrUndefined(value)) {
                    fieldList.add(key);
                } else {
                    fieldList.add(value + AS + key);
                }
            });
            sql.append(StrUtil.join(",", fieldList));
        } else {
            sql.append(ALL_FIELD);
        }

        //拼接from
        sql.append(" FROM ").append(tableName);

        //将时间添加到where中
        if (CollectionUtil.isNotEmpty(timeRange)) {
            JSONObject timeBetween = JSONUtil.createObj().set("field", timeRange.getStr("time_rang_field")).set("operator", "between").set("value", timeRange);
            if (Objects.isNull(whereList)) {
                whereList = JSONUtil.createArray();
            }
            whereList.add(timeBetween);
        }

        //处理where条件
        if (CollectionUtil.isNotEmpty(whereList)) {
            sql.append(" where ").append(ONE_EQ_ONE).append(AND_STR);
            String filterStr = whereList.stream().map(temp -> {
                String whereField = ((JSONObject) temp).getStr("field");
                String operator = ((JSONObject) temp).getStr("operator");
                Object whereValue = ((JSONObject) temp).get("value");
                StringBuilder filterSb = new StringBuilder();
                filterSb.append(whereField).append(SPACE).append(operator);

                if (StrUtil.equals(EQ, operator)) {
                    filterSb.append(" '").append(whereValue).append("' ");
                } else if (StrUtil.equalsIgnoreCase(LIKE, operator)) {
                    filterSb.append(" '%").append(whereValue).append("%' ");
                } else if (StrUtil.equalsIgnoreCase(IN, operator) | StrUtil.equalsIgnoreCase(NOT_IN, operator)) {
                    filterSb.append(" (");
                    if (whereValue instanceof Collection) {
                        String listStr = ((JSONArray) whereValue).stream().map(t -> "'" + t + "'").collect(Collectors.joining(", "));
                        filterSb.append(listStr);
                    } else if (whereValue instanceof String) {
                        filterSb.append(whereValue.toString());
                    }
                    filterSb.append(") ");
                } else if (StrUtil.equalsIgnoreCase(BETWEEN, operator)) {
                    JSONObject betweenValue = JSONUtil.parseObj(whereValue);
                    filterSb.append(" '").append(betweenValue.getStr("since")).append("'")
                            .append(AND_STR).append("'").append(betweenValue.getStr("until")).append("' ");
                } else {
                    throw new RuntimeException("不支持的operator[" + operator + "]");
                }
                return filterSb.toString();
            }).collect(Collectors.joining(AND_STR));
            sql.append(filterStr);
        }

        //拼接group参数
        if (!isSummery && CollectionUtil.isNotEmpty(groupByMap)) {
            sql.append(" GROUP BY ").append(StrUtil.join(",", groupByMap.keySet()));
        }

        //拼接orderBy
        if (!isSummery && CollectionUtil.isNotEmpty(orderBy)) {
            String orderBy_field = orderBy.getStr("field");
            String orderBy_order = orderBy.getStr("order");
            orderBy_order = ASC.equals(orderBy_order) ? "ASC" : "DESC";
            sql.append(" ORDER BY ").append(orderBy_field).append(" ").append(orderBy_order);
        }

        //拼接分页
        if (!isSummery && CollectionUtil.isNotEmpty(limitPage)) {
            int size = limitPage.getInt("size");
            int count = limitPage.getInt("count");
            int limit = Math.max(size, 1);
            int offset = limit * Math.max(count - 1, 0);
            sql.append(" LIMIT ").append(limit).append(" OFFSET ").append(offset);
        }
        return sql.toString();
    }


}
