package symbols;

import inter.Id;

import java.util.Hashtable;

import lexer.Token;

public class Env {
	private Hashtable table;
	protected Env prev;
	
	// ��ʽ���ű�ͬһ�������ڵ�id�洢��ͬһ���ű��ڣ������������Ϊ����ǰһ���ڵ㡣
	public Env(Env n) { table = new Hashtable(); prev=n;}
	
	public void put(Token w, Id i) {
		table.put(w, i);
	}
	
	public Id get(Token w){ //�ڷ��ű��л�ȡid�������ǰ���ű�δ�ҵ�������ǰһ�����ű����
		for(Env e=this; e!=null; e=e.prev){
			Id found = (Id)(e.table.get(w));
			if(found!=null) return found;
		}
		return null;
	}
}
