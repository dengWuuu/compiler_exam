package parser.symbols;

import parser.inter.Id;

import java.util.Hashtable;

import parser.lexer.Token;

public class Env {
	private Hashtable table;
	protected Env prev;
	
	// 链式符号表，同一作用域内的id存储在同一符号表内，外层作用域作为链上前一个节点。
	public Env(Env n) { table = new Hashtable(); prev=n;}
	
	public void put(Token w, Id i) {
		table.put(w, i);
	}
	
	public Id get(Token w){ //在符号表中获取id，如果当前符号表未找到，则在前一个符号表查找
		for(Env e=this; e!=null; e=e.prev){
			Id found = (Id)(e.table.get(w));
			if(found!=null) return found;
		}
		return null;
	}
}
