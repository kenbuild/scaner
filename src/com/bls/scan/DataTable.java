package com.bls.scan;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

 public class DataTable extends JTable 
 { // ʵ���Լ��ı����
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
//	public void allRowsChanged() 
//	{
//		tableRowSorter.allRowsChanged();
//	}

	public DataTable(Vector<Vector<String>> rowData, Vector<String> columnNames) 
	{
		super(rowData, columnNames);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setRowHeight(25);
	}
	
	public DataTable(DefaultTableModel tableModel) 
	{
		super(tableModel);
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		setRowHeight(25);
		TableRowSorter tableRowSorter=new TableRowSorter<>(tableModel);
		setRowSorter(tableRowSorter);
		
		//���÷ּ�״̬Ϊ���񡱵��еĵ�ɫΪ��ɫ
		createDefaultRenderers();
		setDefaultRenderer(Object.class, new ColorTableCellRenderer());  //ColorTableCellRenderer���Խ���
	}
	
	@Override
	public JTableHeader getTableHeader() { // ������ͷ
		// ��ñ��ͷ����
		JTableHeader tableHeader = super.getTableHeader();
		tableHeader.setReorderingAllowed(true); // ���ñ���в�������
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader
				.getDefaultRenderer(); // ��ñ��ͷ�ĵ�Ԫ�����
		// ��������������ʾ
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		return tableHeader;
	}
	
	// ���嵥Ԫ��
	@Override
	public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
		DefaultTableCellRenderer cr = (DefaultTableCellRenderer) super
				.getDefaultRenderer(columnClass); // ��ñ��ĵ�Ԫ�����
		// ���õ�Ԫ�����ݾ�����ʾ
		cr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		return cr;
	}
	
	@Override
	public boolean isCellEditable(int row, int column) { // ��񲻿ɱ༭
		return false;
	}
}