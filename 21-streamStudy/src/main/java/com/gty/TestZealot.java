package com.gty;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.core.ZealotKhala;
import org.junit.Test;

import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

public class TestZealot {
    static HashMap<String, String> alaisMap = CollectionUtil.<String, String>newHashMap();

    static {
        alaisMap.put("average_cpi", "CASE WHEN SUM(installs) > 0 THEN SUM(spend)/SUM(installs)::float8 ELSE null END");
        alaisMap.put("actual_roas_day0", "CASE WHEN SUM(spend) > 0 THEN SUM(revenue_actual_day0)/SUM(spend)::float8 ELSE null END");
        alaisMap.put("profit_pred_day30", "SUM(revenue_pred_day30)-SUM(spend)::float8");
        alaisMap.put("roas_pred_day30", "CASE WHEN SUM(spend) > 0 THEN SUM(revenue_pred_day30)/SUM(spend)::float8 ELSE null END");
        alaisMap.put("profit_actual_day30", "SUM(revenue_actual_day30)-SUM(spend) ::float8");
        alaisMap.put("revenue_actual_day30", "SUM(revenue_actual_day30)");
        alaisMap.put("roas_actual_day30", "CASE WHEN SUM(spend) > 0 THEN SUM(revenue_actual_day30)/SUM(spend)::float8 ELSE null END");
        alaisMap.put("lt_profit_pred", "SUM(lt_revenue_pred)-SUM(spend)::float8");
        alaisMap.put("lt_roas_pred", "CASE WHEN SUM(spend) > 0 THEN SUM(lt_revenue_pred)/SUM(spend)::float8 ELSE null END");
        alaisMap.put("retention", "CASE WHEN SUM(installs) > 0 THEN SUM(retention_day1)/SUM(installs)::float8 ELSE null END");
        alaisMap.put("lt_revenue_pred_factor", "AVG(lt_revenue_pred_factor)");
        alaisMap.put("revenue_pred_factor_30", "AVG(revenue_pred_factor_30)");
    }

    @Test
    public void test01() {
        String tableName = "dws_market_30roi_view";
        JSONArray group = new JSONArray();
        JSONArray field = new JSONArray();
        JSONObject timeRange = new JSONObject();
        JSONArray filtering = new JSONArray();
        JSONObject orderBy = new JSONObject();
        JSONObject limitPage = new JSONObject();

        //配置维度
        group.addAll(CollectionUtil.newArrayList("date", "year", "month", "quarterly", "app_name", "media_source"));

        //配置field
        field.addAll(CollectionUtil.newArrayList("average_cpi", "actual_roas_day0", "profit_pred_day30", "revenue_pred_factor_30"));

        //配置timeRange
        timeRange.set("since", "2020-09-15");
        timeRange.set("until", "2020-10-05");

        //配置filtering
        JSONObject inCondition = new JSONObject();
        inCondition.set("field", "media_source");
        inCondition.set("operator", "in");
        inCondition.set("value", CollectionUtil.newArrayList("google", "facebook", "applovin"));

        JSONObject likeCondition = new JSONObject();
        likeCondition.set("field", "app_name");
        likeCondition.set("operator", "like");
        likeCondition.set("value", "Camp Construction");
        filtering.add(inCondition);
        filtering.add(likeCondition);

        //配置排序
        orderBy.set("field", "date");
        orderBy.set("order", "ascending");

        //配置分页
        limitPage.set("size", 1000);
        limitPage.set("count", 1);


        //开始使用zealot==============================================
        field.addAll(group);
        String fieldStr = field.stream().map(temp -> {
            String alais = alaisMap.get(temp);
            if (Objects.isNull(alais)) {
                return temp + " AS " + temp;
            } else {
                return alais + " AS " + temp;
            }
        }).collect(Collectors.joining(","));

        ZealotKhala from = ZealotKhala.start().select(fieldStr).from(tableName).where(" 1 = 1 ");
        if (CollectionUtil.isNotEmpty(timeRange)) {
            from.andBetween("date", timeRange.getStr("since"), timeRange.getStr("until"));
        }

        if (CollectionUtil.isNotEmpty(filtering)) {
            filtering.stream().map(JSONUtil::parseObj).forEach(temp -> {
                String operator = temp.getStr("operator");
                String field1 = temp.getStr("field");
                if (Objects.equals(operator, "like")) {
                    from.andLike(field1, temp.getStr("value"));
                } else if (Objects.equals(operator, "in")) {
                    from.andIn(field1, temp.getJSONArray("value"));
                } else if (Objects.equals(operator, "=")) {
                    from.andEqual(field1, temp.getStr("value"));
                } else {
                    return;
                }
            });
        }

        from.groupBy(StrUtil.join(",", group));

        if (CollectionUtil.isNotEmpty(orderBy)) {
            String orderBy_field = orderBy.getStr("field");
            String orderBy_order = orderBy.getStr("order");
            orderBy_order = "ascending".equals(orderBy_order) ? "ASC" : "DESC";
            from.orderBy(orderBy_field + " " + orderBy_order);
        }

        if (CollectionUtil.isNotEmpty(limitPage)) {
            int size = limitPage.getInt("size");
            int count = limitPage.getInt("count");
            int limit = Math.max(size, 1);
            int offset = limit * Math.max(count - 1, 0);

            from.limit(Integer.toString(limit));
            from.offset(Integer.toString(offset));
        }


        SqlInfo end = from.end();

        String sql = end.getSql();
        System.out.println(sql);
        Object[] paramsArr = end.getParamsArr();
        for (int i = 0; i < paramsArr.length; i++) {
            System.out.println(paramsArr[i]);
        }

        System.out.println("00000000000000000000000000000000000000000000000000000000000000000000000000000");
/*        SqlInfo sqlInfo = SqlSplitUseZealot.buildDeleteSQL(,tableName, field, group, alaisMap, filtering, orderBy, limitPage);
        String sql11 = sqlInfo.getSql();
        System.out.println(sql11);
        Object[] paramsArr11 = sqlInfo.getParamsArr();
        for (int i = 0; i < paramsArr11.length; i++) {
            System.out.println(paramsArr[i]);

        }*/


    }
}