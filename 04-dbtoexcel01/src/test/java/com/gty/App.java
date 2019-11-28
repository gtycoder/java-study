package com.gty;

import com.gty.dao.UserMapper;
import com.gty.domain.User;
import com.gty.util.CreatExcel;
import com.gty.util.MapAndBeanUtil;
import com.gty.util.WriteExcel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class App {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testCreate() throws Exception {
        List<User> userList = userMapper.getAllUser();
        List<Map<String, Object>> maps = MapAndBeanUtil.beansToMap(userList);
        CreatExcel.writeExcel(maps, "e:\\");
    }

    @Test
    public void testWrite() throws Exception {
        List<User> userList = userMapper.getAllUser();
        List<Map<String, Object>> maps = MapAndBeanUtil.beansToMap(userList);
        WriteExcel.writeExcel(maps,new File("e:\\write.xlsx"));
    }
}
