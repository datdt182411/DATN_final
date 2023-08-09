// Sales Report by Product
var data;
var chartOptions;

$(document).ready(function() {
	setupButtonEventHandlers("_product", loadSalesReportByDateForProduct);	
});

function loadSalesReportByDateForProduct(period) {
	if (period == "custom") {
		startDate = $("#startDate_product").val();
		endDate = $("#endDate_product").val();
		
		requestURL = contextPath + "reports/product/" + startDate + "/" + endDate;
	} else {
		requestURL = contextPath + "reports/product/" + period;		
	}
	
	$.get(requestURL, function(responseJSON) {
		prepareChartDataForSalesReportByProduct(responseJSON);
		customizeChartForSalesReportByProduct();
		formatChartData(data, 3);
		drawChartForSalesReportByProduct(period);
		setSalesAmount(period, '_product', "Tổng số sản phẩm");
	});
}

function prepareChartDataForSalesReportByProduct(responseJSON) {
	data = new google.visualization.DataTable();
	data.addColumn('string', 'Product');
	data.addColumn('string', 'Loại sản phẩm');
	data.addColumn('number', 'Quantity');
	data.addColumn('number', 'Gross Sales');
	
	totalGrossSales = 0;
	totalItems = 0;
	
	$.each(responseJSON, function(index, reportItem) {
		data.addRows([[reportItem.identifier, reportItem.typeName ,reportItem.productCount, reportItem.grossSales]]);
		totalItems += parseInt(reportItem.productCount);
		totalGrossSales += parseFloat(reportItem.grossSales);
	});
}

function customizeChartForSalesReportByProduct() {
	chartOptions = {
		height: 360, width: '98%',
		showRowNumber: true,
		page: 'enable',
		sortColumn: 2,
		sortAscending: false
	};
}

function drawChartForSalesReportByProduct() {
	var salesChart = new google.visualization.Table(document.getElementById('chart_sales_by_product'));
	salesChart.draw(data, chartOptions);
}