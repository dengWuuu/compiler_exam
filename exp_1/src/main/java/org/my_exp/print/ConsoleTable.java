package org.my_exp.print;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 输出工具类：参考网上
 * @author Wu
 */
public class ConsoleTable {

	private final LinkedList<List<Object>> rows = new LinkedList<>();

	private final int colum;

	private final int[] columLen;

	private final boolean printHeader;

	public ConsoleTable(int colum, boolean printHeader) {
		this.printHeader = printHeader;
		this.colum = colum;
		this.columLen = new int[colum];
	}

	/**
	 * create everyRow in the graph
	 */
	public void appendRow() {
		if (!rows.isEmpty()) {
			List<Object> temp = rows.getLast();
			if (temp.isEmpty())
				return;
		}
		List<Object> row = new ArrayList<>(colum);
		rows.add(row);
	}

	/*
	 * crete everyCol in the graph
	 */
	public void appendColum(Object value) {
		if (value == null) {
			value = "NULL";
		}
		List<Object> row = rows.get(rows.size() - 1);
		row.add(value);
		int len = value.toString().getBytes().length;
		if (columLen[row.size() - 1] < len)
			columLen[row.size() - 1] = len;
	}

	/**
	 * 核心方法 ， 将数据结构转换成可视化的表格
	 * @return
	 */
	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();

		int sumlen = 0;
		for (int len : columLen) {
			sumlen += len;
		}
		int margin = 2;
		if (printHeader)
			buf.append("|").append(printChar('=', sumlen + margin * 2 * colum + (colum - 1))).append("|\n");
		else
			buf.append("|").append(printChar('-', sumlen + margin * 2 * colum + (colum - 1))).append("|\n");
		for (int ii = 0; ii < rows.size(); ii++) {
			List<Object> row = rows.get(ii);
			for (int i = 0; i < colum; i++) {
				String o = "";
				if (i < row.size())
					o = row.get(i).toString();
				buf.append('|').append(printChar(' ', margin)).append(o);
				buf.append(printChar(' ', columLen[i] - o.getBytes().length + margin));
			}
			buf.append("|\n");
			if (printHeader && ii == 0)
				buf.append("|").append(printChar('=', sumlen + margin * 2 * colum + (colum - 1))).append("|\n");
			else
				buf.append("|").append(printChar('-', sumlen + margin * 2 * colum + (colum - 1))).append("|\n");
		}
		return buf.toString();
	}

	private String printChar(char c, int len) {
		return String.valueOf(c).repeat(Math.max(0, len));
	}

}