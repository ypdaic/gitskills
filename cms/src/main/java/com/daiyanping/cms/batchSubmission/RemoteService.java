package com.daiyanping.cms.batchSubmission;

import com.daiyanping.cms.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RemoteService {

	public User getUser(Integer id) {
		User user = new User();
		user.setId(id);
		return user;
	}

	public List<Map<String, Object>> getUsers(ArrayList<Integer> ids) {
		ArrayList<Map<String, Object>> maps = new ArrayList<>();

		ids.forEach(id->{
			HashMap<String, Object> map = new HashMap<>();
			map.put("id", id);
			map.put("value", "结果" + id);
			maps.add(map);
		});

		return maps;
	}
}
