package com.bls.scan;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

 public class DataTable extends JTable 
 { // 实现自己的表格类
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
		
		//设置分拣状态为“否”的行的底色为黄色
		createDefaultRenderers();
		setDefaultRenderer(Object.class, new ColorTableCellRenderer());  //ColorTableCellRenderer是自建类
	}
	
	@Override
	public JTableHeader getTableHeader() { // 定义表格头
		// 获得表格头对象
		JTableHeader tableHeader = super.getTableHeader();
		tableHeader.setReorderingAllowed(true); // 设置表格列不可重排
		DefaultTableCellRenderer hr = (DefaultTableCellRenderer) tableHeader
				.getDefaultRenderer(); // 获得表格头的单元格对象
		// 设置列名居中显示
		hr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		return tableHeader;
	}
	
	// 定义单元格
	@Override
	public TableCellRenderer getDefaultRenderer(Class<?> columnClass) {
		DefaultTableCellRenderer cr = (DefaultTableCellRenderer) super
				.getDefaultRenderer(columnClass); // 获得表格的单元格对象
		// 设置单元格内容居中显示
		cr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		return cr;
	}
	
	@Override
	public boolean isCellEditable(int row, int column) { // 表格不可编辑
		return false;
	}
}