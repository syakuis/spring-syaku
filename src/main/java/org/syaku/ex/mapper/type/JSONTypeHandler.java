package org.syaku.ex.mapper.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author Seok Kyun. Choi. 최석균 (Syaku)
 * @site http://syaku.tistory.com
 * @since 16. 3. 8.
 */
public class JSONTypeHandler extends BaseTypeHandler<Object> {

	private static String CHARSET = "utf-8";

	public static Map<String, Object> toMap(String json) {
		Map<String, Object> result = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			result = mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static String toString(Object object) {
		return toString(object, CHARSET);
	}
	public static String toString(Object object, String charset) {
		ByteArrayOutputStream output = null;
		Writer write = null;
		String data = null;

		try{
			output = new ByteArrayOutputStream();
			write = new OutputStreamWriter(output, charset);

			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(write, object);
			data = output.toString(charset);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if(output != null) try { output.close(); } catch (IOException e) { }
			if(write != null) try { write.close(); } catch (IOException e) { }
		}

		return data;
	}

	@Override
	public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)
			throws SQLException {

		String p = toString(parameter);
		ps.setObject(i, p);
	}

	@Override
	public Object getNullableResult(ResultSet rs, String columnName)
			throws SQLException {
		Object d = rs.getObject(columnName);
		if(d == null) return d;
		return toMap(d.toString());
	}

	@Override
	public Object getNullableResult(ResultSet rs, int columnIndex)
			throws SQLException {
		Object d = rs.getObject(columnIndex);
		if(d == null) return d;
		return toMap(d.toString());
	}

	@Override
	public Object getNullableResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		Object d = cs.getObject(columnIndex);
		if(d == null) return d;
		return toMap(d.toString());
	}
}
