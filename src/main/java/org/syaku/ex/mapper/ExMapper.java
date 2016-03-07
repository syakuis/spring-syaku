package org.syaku.ex.mapper;

import org.mybatis.spring.annotation.MapperScan;
import org.syaku.ex.dao.ExDAO;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 3. 7.
 */
@MapperScan
public interface ExMapper extends ExDAO {
}
