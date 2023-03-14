package com.climb.monitoring;

import com.climb.monitoring.entity.User;
import com.climb.monitoring.service.UserCacheService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MonitoringPlatformApplication.class)
@ExtendWith(SpringExtension.class)
class MonitoringPlatformApplicationTests {

	@Autowired
	private UserCacheService userCacheService;

	@Test
	public void test() throws Exception{
		Long id = 1L;
		User user = userCacheService.getUser(id);
		User user2 = userCacheService.getUser(id);
		System.out.println("查询其他数据");
		User user3 = userCacheService.getUser(2l);
		User user4 = userCacheService.getUser(2l);
		System.out.println("进行删除");
		// 删除 id为1的数据
		userCacheService.delUser(user);
		User user7 = userCacheService.getUser(1l);
		User user5 = userCacheService.getUser(2l);
		User user6 = userCacheService.getUser(2l);


	}
}
