package com.gty;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.blinkfox.zealot.bean.SqlInfo;
import com.blinkfox.zealot.core.ZealotKhala;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 使用zealot开源项目实现SQL的拼接
 */
public class SqlSplitUseZealot {

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

    //===================================以下是删除的SQL==============================================
    public static SqlInfo buildDeleteSQL(String tableName, JSONArray whereList) {
        tableName = tableName.trim();
        if (CollectionUtil.isEmpty(whereList)) {
            throw new RuntimeException("请配置筛选条件");
        }
        ZealotKhala from = ZealotKhala.start().deleteFrom(tableName).where(ONE_EQ_ONE);
        whereList.stream().map(JSONUtil::parseObj).forEach(temp -> {
            String operator = temp.getStr("operator");
            String whereField = temp.getStr("field");
            Object whereValue = temp.get("value");
            if (StrUtil.equalsIgnoreCase(operator, LIKE)) {
                from.andLike(whereField, whereValue);
            } else if (StrUtil.equalsIgnoreCase(operator, NOT_IN) | StrUtil.equalsIgnoreCase(operator, IN)) {
                processCollectionValue(from, whereField, operator, whereValue);
            } else if (StrUtil.equalsIgnoreCase(operator, EQ)) {
                from.andEqual(whereField, whereValue);
            } else if (StrUtil.equalsIgnoreCase(operator, BETWEEN)) {
                JSONObject betweenValue = JSONUtil.parseObj(whereValue);
                from.andBetween(betweenValue.getStr("time_rang_field"), betweenValue.getStr("since"), betweenValue.getStr("until"));
            } else {
                throw new RuntimeException("不支持的操作类型 [" + operator + "] ");
            }
        });
        return from.end();
    }


    //===================================以下是插入的SQL==============================================

    public static SqlInfo buildInsertSQL(String tableName, JSONObject insertObj) {
        List<Object> arr = new ArrayList<>();
        SqlInfo sqlInfo = SqlInfo.newInstance();
        tableName = tableName.trim();
        StringBuilder sql = new StringBuilder();

        sql.append("insert into \"");
        sql.append(tableName).append("\" (");
        StringBuilder temp = new StringBuilder();
        temp.append(") values(");

        insertObj.forEach((key, value) -> {
            if (arr.size() > 0) {
                sql.append(", ");
                temp.append(", ");
            }
            sql.append('\"').append(key).append('\"');
            temp.append('?');
            arr.add(value);
        });
        sql.append(temp.toString()).append(')');
        return sqlInfo.setSql(sql.toString()).setParams(arr);
    }


    //===================================以下是更新的SQL==============================================

    public static SqlInfo buildUpdateSQL(String tableName, List<String> allFieldList, JSONObject updateObj) {
        List<Object> arr = new ArrayList<>();
        ZealotKhala update = ZealotKhala.start().update(tableName);
        if (CollectionUtil.isNotEmpty(allFieldList)) {
            //以allField为主进行SQL拼接
            allFieldList.forEach(temp -> {
                Object value = updateObj.get(temp);
                if (value instanceof Collection | value instanceof Map) {
                    value = JSONUtil.toJsonStr(value);
                }
                update.set(temp + " = ? ");
                arr.add(value);
            });
        } else {
            //以saveObj为主
            updateObj.forEach((temp, value) -> {
                if (value instanceof Collection | value instanceof Map) {
                    value = JSONUtil.toJsonStr(value);
                }
                update.set(temp + " = ? ");
                arr.add(value);
            });
        }
        SqlInfo info = update.end();
        info.setParams(arr);
        return info;
    }


    //=====================================以下是查询的SQL=============================================

    public static SqlInfo buildSelectSQL(Boolean isSummery, String tableName,
                                         JSONArray fieldList, JSONArray groupList, Map<String, String> aliasOrFormulaMap,
                                         JSONObject timeRange, JSONArray whereList, JSONObject orderBy, JSONObject limitPage) {
        return buildSelectSQL(false, isSummery, false, tableName, fieldList, groupList, aliasOrFormulaMap, timeRange, whereList, orderBy, limitPage);
    }

    public static SqlInfo buildSelectSQL(Boolean isSummery, String tableName,
                                         JSONArray fieldList, JSONArray groupList, Map<String, String> aliasOrFormulaMap,
                                         JSONObject timeRange, JSONArray whereList) {
        return buildSelectSQL(false, isSummery, false, tableName, fieldList, groupList, aliasOrFormulaMap, timeRange, whereList, null, null);
    }

    public static SqlInfo buildSelectSQL(Boolean isSummery, String tableName,
                                         JSONArray fieldList, JSONArray groupList) {
        return buildSelectSQL(false, isSummery, false, tableName, fieldList, groupList, null, null, null, null, null);
    }


    /**
     * 最核心的底层方法
     *
     * @param needCount         是否需要total_count总数
     * @param isSummery         是否需要求和
     * @param fieldNeedSum      字段是否需要使用sum(字段)
     * @param tableName         表名
     * @param fieldList         查询字段列表,存储别名
     * @param groupList         分组字段列表,存储别名
     * @param aliasOrFormulaMap 字段别名或是公式列表,使用形式为 <前端别名k,公式或是数据库字段>,拼接形式为 v as k;
     * @param whereList         筛选列表
     * @param orderBy           排序列表
     * @param limitPage         分页
     * @return
     */
    public static SqlInfo buildSelectSQL(Boolean needCount, Boolean isSummery, Boolean fieldNeedSum, String tableName,
                                         JSONArray fieldList, JSONArray groupList, Map<String, String> aliasOrFormulaMap,
                                         JSONObject timeRange, JSONArray whereList, JSONObject orderBy, JSONObject limitPage) {
        //将aliasOrFormulaMap复制一份
        HashMap<String, String> aliasCopyMap = CollectionUtil.<String, String>newHashMap();
        if (CollectionUtil.isNotEmpty(aliasOrFormulaMap)) {
            aliasCopyMap.putAll(aliasOrFormulaMap);
        }

        //将group加入到fields中
        ArrayList<Object> fields = CollectionUtil.newArrayList();
        String fieldStr = "";

        //是否需要条数,只支持postgre
        if (needCount) {
            fields.add("total_count");
            aliasCopyMap.put("total_count", "COUNT(1) OVER()");
        }

        if (!isSummery && CollectionUtil.isNotEmpty(groupList)) {
            fields.addAll(groupList);
        }

        if (CollectionUtil.isNotEmpty(fieldList)) {
            fields.addAll(fieldList);
            //处理isSummery情况
            if (isSummery | fieldNeedSum) {
                //处理sum(字段)加到aliasCopyMap中
                fieldList.forEach(temp -> {
                    String exist = aliasCopyMap.get(temp.toString());
                    if (Objects.isNull(exist)) {
                        aliasCopyMap.put(temp.toString(), "SUM(" + temp + ")");
                    }
                });
            }
        }

        //处理fields
        if (CollectionUtil.isNotEmpty(fields)) {
            if (CollectionUtil.isNotEmpty(aliasCopyMap)) {
                fieldStr = fields.stream().distinct().map(t -> {
                    String fieldOrFormula = aliasCopyMap.remove(t.toString());
                    if (StrUtil.isEmptyOrUndefined(fieldOrFormula)) {
                        return t + AS + t;
                    } else {
                        return fieldOrFormula + AS + t;
                    }
                }).collect(Collectors.joining(","));
            } else {
                fieldStr = StrUtil.join(",", fields);
            }
        } else {
            fieldStr = ALL_FIELD;
        }

        //开始拼接SQL
        ZealotKhala from = ZealotKhala.start().select(fieldStr).from(tableName);

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
            from.where(ONE_EQ_ONE);
            whereList.stream().map(JSONUtil::parseObj).forEach(temp -> {
                String operator = temp.getStr("operator");
                String whereField = temp.getStr("field");
                Object whereValue = temp.get("value");
                if (StrUtil.equalsIgnoreCase(operator, LIKE)) {
                    from.andLike(whereField, whereValue);
                } else if (StrUtil.equalsIgnoreCase(operator, NOT_IN) | StrUtil.equalsIgnoreCase(operator, IN)) {
                    processCollectionValue(from, whereField, operator, whereValue);
                } else if (StrUtil.equalsIgnoreCase(operator, EQ)) {
                    from.andEqual(whereField, whereValue);
                } else if (StrUtil.equalsIgnoreCase(operator, BETWEEN)) {
                    JSONObject betweenValue = JSONUtil.parseObj(whereValue);
                    from.andBetween(betweenValue.getStr("time_rang_field"), betweenValue.getStr("since"), betweenValue.getStr("until"));
                } else {
                    throw new RuntimeException("不支持的操作类型 [" + operator + "] ");
                }
            });
        }

        //处理group
        if (!isSummery && CollectionUtil.isNotEmpty(groupList)) {
            from.groupBy(StrUtil.join(",", groupList));
        }

        //处理order
        if (!isSummery && CollectionUtil.isNotEmpty(orderBy)) {
            String orderField = orderBy.getStr("field");
            String orderType = orderBy.getStr("order");
            if (Objects.equals(orderType, ASC)) {
                from.orderBy(orderField).asc();
            } else {
                from.orderBy(orderField).desc();
            }
        }

        //处理limit
        if (!isSummery && CollectionUtil.isNotEmpty(limitPage)) {
            int size = limitPage.getInt("size");
            int count = limitPage.getInt("count");
            int limit = Math.max(size, 1);
            int offset = limit * Math.max(count - 1, 0);
            from.limit(Integer.toString(limit));
            from.offset(Integer.toString(offset));
        }

        return from.end();
    }


    private static void processCollectionValue(ZealotKhala from, String whereField, String operator, Object whereValue) {
        if (whereValue instanceof Collection) {
            if (StrUtil.equalsIgnoreCase(operator, IN)) {
                from.andIn(whereField, JSONUtil.parseArray(whereValue));
            } else if (StrUtil.equalsIgnoreCase(operator, NOT_IN)) {
                from.andNotIn(whereField, JSONUtil.parseArray(whereValue));
            }
        } else if (whereValue instanceof String) {
            from.doAnything((sb, param) -> {
                sb.append(whereField).append(SPACE).append(operator).append(" (").append(whereValue.toString()).append(") ");
            });
        } else if (whereValue instanceof SqlInfo) {
            SqlInfo portSqlInfo = (SqlInfo) whereValue;
            from.doAnything((sb, param) -> {
                sb.append(whereField).append(SPACE).append(operator).append(" (").append(portSqlInfo.getSql()).append(") ");
                param.addAll(portSqlInfo.getParams());
            });
        } else {
            throw new RuntimeException("不支持的操作类型 [" + operator + "] ");
        }
    }
}
