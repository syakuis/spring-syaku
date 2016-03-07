package org.syaku.ex.service;

import org.springframework.stereotype.Service;
import org.syaku.ex.dao.ExDAO;
import org.syaku.ex.domain.Ex;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 3. 7.
 */
@Service
public class ExService {
	@Resource(name = "exMapper") ExDAO exDAO;

	public List<Ex> getExList() {
		return exDAO.select();
	}

	public void insert(Ex ex) {
		exDAO.insert(ex);
	}
}
