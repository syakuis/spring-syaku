package org.syaku.ex.dao;

import org.syaku.ex.domain.Ex;

import java.util.List;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 3. 7.
 */
public interface ExDAO {
	List<Ex> select();
	void insert(Ex ex);
}
